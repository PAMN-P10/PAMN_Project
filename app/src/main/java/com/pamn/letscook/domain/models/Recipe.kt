package com.pamn.letscook.domain.models

import java.time.LocalDateTime

data class Recipe(
    val title: String,
    val description: String,
    val author: User?,
    val difficulty: String,  // Cambiado para representar la dificultad como un String (Beginner, Intermediate, etc.)
    val preparationTime: String,  // Cambiado para manejar tiempo en formato string
    val allergens: List<String> = emptyList(), // Listado de alergenos
    val ingredients: List<Ingredient>,
    val steps: List<PreparationStep>,
    val mainImage: Image? = null, // Imagen principal de la receta
    val servings: Int = 4,
    val appliedFilters: List<FilterLabels> = emptyList(),
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val isFavorite: Boolean = false
) {
    fun adjustServings(newServings: Int): Recipe {
        // Recalcular las cantidades de los ingredientes con respecto a las porciones
        val adjustedIngredients = ingredients.map { ingredient ->
            ingredient.copy(
                quantity = ingredient.quantity * (newServings.toDouble() / servings)
            )
        }

        return copy(
            ingredients = adjustedIngredients,
            servings = newServings
        )
    }

    fun applyFilters(filters: List<FilterLabels>): Recipe {
        return copy(appliedFilters = filters)
    }

    fun toMap(): Map<String, Any?> {
        return mapOf(
            "title" to title,
            "description" to description,
            "difficulty" to difficulty,
            "preparationTime" to preparationTime,
            "allergens" to allergens,
            "ingredients" to ingredients.map { it.toMap() },  // Mapea los ingredientes
            "steps" to steps.map { it.toMap() },  // Mapea los pasos
            "mainImage" to mainImage?.url,
            "servings" to servings,
            "createdAt" to createdAt.toString(),
            "isFavorite" to isFavorite
        )
    }
}
