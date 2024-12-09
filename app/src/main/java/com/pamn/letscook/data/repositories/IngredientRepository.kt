package com.pamn.letscook.data.repositories

import com.google.firebase.firestore.FirebaseFirestore
import com.pamn.letscook.domain.models.Image
import com.pamn.letscook.domain.models.Ingredient
import com.pamn.letscook.domain.models.IngredientType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.tasks.await

class IngredientRepository(private val firestore: FirebaseFirestore) {

    // Función para guardar un ingrediente individual
    suspend fun saveIngredient(ingredient: Ingredient): Result<Unit> {
        return try {
            withContext(Dispatchers.IO) {
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

                firestore.collection("ingredients")
                    .document(ingredient.name) // Usar el nombre como ID del documento
                    .set(ingredientMap)
                    .await()

                Result.success(Unit)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Función para guardar múltiples ingredientes
    suspend fun saveIngredients(ingredients: List<Ingredient>): Result<Unit> {
        return try {
            withContext(Dispatchers.IO) {
                val batch = firestore.batch()

                ingredients.forEach { ingredient ->
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

                    val docRef = firestore.collection("ingredients")
                        .document(ingredient.name)

                    batch.set(docRef, ingredientMap)
                }

                batch.commit().await()
                Result.success(Unit)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Función para leer todos los ingredientes
    suspend fun getAllIngredients(): Result<List<Ingredient>> {
        return try {
            withContext(Dispatchers.IO) {
                val snapshot = firestore.collection("ingredients")
                    .get()
                    .await()

                val ingredients = snapshot.documents.map { document ->
                    // Obtener el mapa de imagen directamente del documento
                    val imageData = document.get("image") as? Map<String, Any>

                    Ingredient(
                        name = document.getString("name") ?: "",
                        type = IngredientType.valueOf(document.getString("type") ?: "Other"),
                        quantity = document.getDouble("quantity") ?: 0.0,
                        isAllergen = document.getBoolean("isAllergen") ?: false,
                        //imageUrl = document.getString("imageUrl")
                        image = Image(
                            label = imageData?.get("label") as? String ?: "",
                            url = imageData?.get("url") as? String ?: "",
                            format = imageData?.get("format") as? String,
                            width = (imageData?.get("width") as? Number)?.toInt(),
                            height = (imageData?.get("height") as? Number)?.toInt()
                        )
                    )
                }

                Result.success(ingredients)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Función para leer un solo ingrediente por nombre (ID del documento)
    suspend fun getIngredientByName(name: String): Result<Ingredient> {
        return try {
            withContext(Dispatchers.IO) {
                val documentSnapshot = firestore.collection("ingredients")
                    .document(name) // Buscar por el nombre como ID
                    .get()
                    .await()

                if (documentSnapshot.exists()) {
                    val imageData = documentSnapshot.get("image") as? Map<String, Any>

                    val ingredient = Ingredient(
                        name = documentSnapshot.getString("name") ?: "",
                        type = IngredientType.valueOf(documentSnapshot.getString("type") ?: "Other"),
                        quantity = documentSnapshot.getDouble("quantity") ?: 0.0,
                        isAllergen = documentSnapshot.getBoolean("isAllergen") ?: false,
                        //imageUrl = documentSnapshot.getString("imageUrl")
                        image = Image(
                            label = imageData?.get("label") as? String ?: "",
                            url = imageData?.get("url") as? String ?: "",
                            format = imageData?.get("format") as? String,
                            width = (imageData?.get("width") as? Number)?.toInt(),
                            height = (imageData?.get("height") as? Number)?.toInt()
                        )
                    )
                    Result.success(ingredient)
                } else {
                    // Si no existe el documento
                    Result.failure(Exception("Ingrediente no encontrado"))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
