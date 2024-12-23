package com.pamn.letscook.data.repositories

import com.google.firebase.firestore.FirebaseFirestore
import com.pamn.letscook.domain.models.Image
import com.pamn.letscook.domain.models.Ingredient
import com.pamn.letscook.domain.models.IngredientType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.tasks.await

class IngredientRepository(
    private val firestore: FirebaseFirestore
) {

    // Enum para categorizar tipos de errores
    sealed class IngredientError : Exception() {
        object NetworkError : IngredientError() {
            private fun readResolve(): Any = NetworkError
        }

        object DatabaseError : IngredientError() {
            private fun readResolve(): Any = DatabaseError
        }

        data class NotFoundError(override val message: String) : IngredientError()
        data class ParseError(override val message: String) : IngredientError()
    }

    // Función para guardar un ingrediente individual
    suspend fun saveIngredient(ingredient: Ingredient): Result<Unit> {
        return runCatching {
            withContext(Dispatchers.IO) {
                // Validaciones previas
                require(ingredient.name.isNotBlank()) { "El nombre del ingrediente no puede estar vacío" }
                require(ingredient.quantity >= 0) { "La cantidad no puede ser negativa" }

                val ingredientMap = mapOf(
                    "name" to ingredient.name,
                    "type" to ingredient.type.name,
                    "quantity" to ingredient.quantity,
                    "isAllergen" to ingredient.isAllergen,
                    //"imageUrl" to ingredient.imageUrl
                    "image" to mapOf(
                        "label" to ingredient.image.label,
                        "url" to ingredient.image.url,
                        "format" to ingredient.image.format,
                        "width" to ingredient.image.width,
                        "height" to ingredient.image.height
                    )
                )

                try {
                    firestore.collection("ingredients")
                        .document(ingredient.name)
                        .set(ingredientMap)
                        .await()

                    // Explícitamente devolver Unit porque .set() devuelve un Task<Void>
                    Unit
                } catch (e: Exception) {
                    throw IngredientError.DatabaseError
                    println("Error al guardar el ingrediente en la base de datos")// Imprimir por consola e

                }
            }
        }.onFailure { error ->
            // Logging de errores (en un sistema real, usarías un logger)
            println("Error al guardar ingrediente: ${error.message}")
        }
    }

    // Función para guardar múltiples ingredientes
    suspend fun saveIngredients(ingredients: List<Ingredient>): Result<Unit> {
        return runCatching {
            withContext(Dispatchers.IO) {
                // Validaciones previas
                require(ingredients.isNotEmpty()) { "La lista de ingredientes no puede estar vacía" }

                // Validar cada ingrediente antes de intentar guardar
                ingredients.forEach { ingredient ->
                    require(ingredient.name.isNotBlank()) { "El nombre del ingrediente no puede estar vacío" }
                    require(ingredient.quantity >= 0) { "La cantidad no puede ser negativa" }
                }

                // Llama a saveIngredient para cada ingrediente
                ingredients.forEach { ingredient ->
                    val result = saveIngredient(ingredient)
                    if (result.isFailure) {
                        throw result.exceptionOrNull() ?: IngredientError.DatabaseError
                    }
                }

                println("Todos los ingredientes se han guardado con éxito.")
            }
        }.onFailure { error ->
            println("Error al guardar múltiples ingredientes: ${error.message}")
        }
    }

    // Función para leer todos los ingredientes
    suspend fun getAllIngredients(): Result<List<Ingredient>> {
        return runCatching {
            withContext(Dispatchers.IO) {
                val snapshot = try {
                    firestore.collection("ingredients")
                        .get()
                        .await()
                } catch (e: Exception) {
                    throw IngredientError.NetworkError
                }

                // Filtrar y mapear solo los documentos válidos
                val ingredients = snapshot.documents.mapNotNull { document ->
                    try {
                        val imageData = document.get("image") as? Map<String, Any>

                        // Validaciones de datos
                        val name = document.getString("name")
                        val type = document.getString("type")

                        if (name.isNullOrBlank() || type.isNullOrBlank()) {
                            null // Saltar documentos inválidos
                        } else {
                            Ingredient(
                                name = name,
                                type = IngredientType.valueOf(type),
                                quantity = document.getDouble("quantity") ?: 0.0,
                                isAllergen = document.getBoolean("isAllergen") ?: false,
                                image = Image(
                                    label = imageData?.get("label") as? String ?: "",
                                    url = imageData?.get("url") as? String ?: "",
                                    format = imageData?.get("format") as? String,
                                    width = (imageData?.get("width") as? Number)?.toInt() ?: 0,
                                    height = (imageData?.get("height") as? Number)?.toInt() ?: 0
                                )
                            )
                        }
                    } catch (e: Exception) {
                        // Log del documento problemático sin detener toda la operación
                        println("Error procesando documento: ${document.id}")
                        null
                    }
                }

                // Verificar si la lista de ingredientes está vacía
                if (ingredients.isEmpty()) {
                    throw IngredientError.NotFoundError("No se encontraron ingredientes")
                }

                ingredients
            }
        }.onFailure { error ->
            when (error) {
                is IngredientError.NetworkError ->
                    println("Error de red al recuperar ingredientes")
                is IngredientError.NotFoundError ->
                    println("No se encontraron ingredientes")
                is IngredientError.ParseError ->
                    println("Error al parsear los datos de ingredientes")
                else ->
                    println("Error desconocido: ${error.message}")
            }
        }
    }

    // Función para leer un solo ingrediente por nombre (ID del documento)
    suspend fun getIngredientByName(name: String): Result<Ingredient> {
        return runCatching {
            withContext(Dispatchers.IO) {
                val documentSnapshot = try {
                    firestore.collection("ingredients")
                        .document(name)
                        .get()
                        .await()
                } catch (e: Exception) {
                    throw IngredientError.NetworkError
                }

                if (!documentSnapshot.exists()) {
                    throw IngredientError.NotFoundError("Ingrediente no encontrado: $name")
                }

                try {
                    val imageData = documentSnapshot.get("image") as? Map<String, Any>

                    Ingredient(
                        name = documentSnapshot.getString("name") ?:
                        throw IngredientError.ParseError("Nombre inválido"),
                        type = IngredientType.valueOf(
                            documentSnapshot.getString("type") ?:
                            throw IngredientError.ParseError("Tipo inválido")
                        ),
                        quantity = documentSnapshot.getDouble("quantity") ?: 0.0,
                        isAllergen = documentSnapshot.getBoolean("isAllergen") ?: false,
                        image = Image(
                            label = imageData?.get("label") as? String ?: "",
                            url = imageData?.get("url") as? String ?: "",
                            format = imageData?.get("format") as? String,
                            width = (imageData?.get("width") as? Number)?.toInt() ?: 0,
                            height = (imageData?.get("height") as? Number)?.toInt() ?: 0
                        )
                    )
                } catch (e: Exception) {
                    throw IngredientError.ParseError("Error al parsear el ingrediente")
                }
            }
        }.onFailure { error ->
            when (error) {
                is IngredientError.NetworkError ->
                    println("Error de red al recuperar ingrediente")
                is IngredientError.NotFoundError ->
                    println("Ingrediente no encontrado")
                is IngredientError.ParseError ->
                    println("Error al parsear los datos del ingrediente")
                else ->
                    println("Error desconocido: ${error.message}")
            }
        }
    }
}