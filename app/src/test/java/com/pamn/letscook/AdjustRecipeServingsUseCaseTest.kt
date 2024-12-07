package com.pamn.letscook

import com.pamn.letscook.domain.models.*
import com.pamn.letscook.domain.useCases.AdjustRecipeServingsUseCase
import org.junit.Assert.assertEquals
import org.junit.Test

class AdjustRecipeServingsUseCaseTest {

    private val adjustRecipeServingsUseCase = AdjustRecipeServingsUseCase()

    @Test
    fun `adjustServings should recalculate ingredient quantities`() {
        // Arrange
        val recipe = Recipe(
            id = "1",
            title = "Pasta",
            description = "Delicious pasta recipe",
            author = User("1", "User1", "user@example.com", "password123"),
            ingredients = listOf(
                Ingredient(name = "Pasta", type = IngredientType.Pasta, quantity = 200.0),
                Ingredient(name = "Tomato Sauce", type = IngredientType.Vegetable, quantity = 100.0)
            ),
            steps = emptyList(),
            preparationTime = 30,
            difficulty = DifficultyLevel.Beginner,
            mainImage = null,
            servings = 2
        )

        // Act
        val updatedRecipe = adjustRecipeServingsUseCase(recipe, newServings = 4)

        // Assert
        assertEquals(400.0, updatedRecipe.ingredients[0].quantity, 0.1) // Pasta
        assertEquals(200.0, updatedRecipe.ingredients[1].quantity, 0.1) // Tomato Sauce
        assertEquals(4, updatedRecipe.servings)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `adjustServings should throw exception for invalid servings`() {
        // Arrange
        val recipe = Recipe(
            id = "1",
            title = "Pasta",
            description = "Delicious pasta recipe",
            author = User("1", "User1", "user@example.com", "password123"),
            ingredients = listOf(),
            steps = emptyList(),
            preparationTime = 30,
            difficulty = DifficultyLevel.Beginner,
            mainImage = null,
            servings = 2
        )

        // Act
        adjustRecipeServingsUseCase(recipe, newServings = 0) // Invalid servings
    }
}
