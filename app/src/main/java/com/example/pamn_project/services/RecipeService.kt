// File: app/src/main/java/com/example/pamn_project/services/RecipeService.kt

package com.example.pamn_project.services

import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

object RecipeService {
    private val database = FirebaseDatabase.getInstance().reference.child("users")

    data class Ingredient(
        val name: String,
        var quantity: Double,
        var unit: String
    )

    /**
     * Guarda una receta en el nodo correspondiente al usuario autenticado.
     */
    suspend fun saveRecipe(
        userId: String,
        title: String,
        description: String,
        imageBase64: String?,
        ingredients: List<Ingredient>,
        instructions: List<String>,
        cookingTime: String?
    ): Result<Unit> {
        return try {
            val recipeId = database.child(userId).child("recipes").push().key ?: throw Exception("Failed to generate recipe ID")

            // Construir map de ingredientes
            val ingredientsMap = ingredients.associate { ingredient ->
                ingredient.name to mapOf(
                    "quantity" to ingredient.quantity,
                    "unit" to ingredient.unit
                )
            }

            // Construir map de instrucciones con pasos numerados
            val instructionsMap = instructions.mapIndexed { index, step ->
                (index + 1).toString() to step
            }.toMap()

            // Construir map de timer
            val timerMap = mapOf(
                "exists" to (cookingTime != null && cookingTime.isNotEmpty()),
                "time" to (cookingTime ?: "")
            )

            // Estructura de la receta
            val recipeData = mapOf(
                "title" to title,
                "description" to description,
                "image" to (imageBase64 ?: ""),
                "ingredients" to ingredientsMap,
                "instructions" to instructionsMap,
                "timer" to timerMap
            )

            // Guardar en la base de datos
            database.child(userId).child("recipes").child(recipeId).setValue(recipeData).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
