package com.pamn.letscook.domain.models

data class Recipe(
    val id: String? = null,
    val title: String,
    val description: String,
    val author: User,
    val ingredients: List<Ingredient>,
    val steps: List<PreparationStep>,
    val preparationTime: Int,
    val difficulty: DifficultyLevel,
    val mainImage: Image? = null, // no es image de android media sino el objeto Image creado
    val servings: Int = 4,
    val appliedFilters: List<FilterLabels> = emptyList() // filtros para la busqueda

) {
    fun adjustServings(newServings: Int): Recipe {
        // Recalculate ingredient quantities based on new servings
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
}