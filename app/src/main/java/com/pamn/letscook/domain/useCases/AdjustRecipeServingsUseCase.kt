package com.pamn.letscook.domain.useCases

import com.pamn.letscook.domain.models.Recipe

class AdjustRecipeServingsUseCase {
    operator fun invoke(recipe: Recipe, newServings: Int): Recipe {
        val adjustedIngredients = recipe.ingredients.map { ingredient ->
            ingredient.copy(
                quantity = ingredient.quantity * (newServings.toDouble() / recipe.servings)
            )
        }

        return recipe.copy(
            ingredients = adjustedIngredients,
            servings = newServings
        )
    }
}
