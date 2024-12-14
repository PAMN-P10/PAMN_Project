package com.pamn.letscook.domain.usecases

import com.pamn.letscook.data.repositories.IngredientRepository
import com.pamn.letscook.domain.models.Ingredient
import com.pamn.letscook.domain.models.Image
import com.pamn.letscook.domain.models.IngredientType

class IngredientInitializer(private val repository: IngredientRepository) {

    // Función para inicializar datos
    suspend fun initializeIngredientsIfEmpty() {
        println("Inicializando ingredientes...")

        val result = repository.getAllIngredients()

        result.onSuccess { ingredients ->
            println("Ingredientes existentes: $ingredients")
            if (ingredients.isEmpty()) {
                saveInitialIngredients()
            }
        }.onFailure { error ->
            println("Error al obtener ingredientes: ${error.message}")
            saveInitialIngredients()
        }
    }

    private suspend fun saveInitialIngredients() {
        val initialIngredients = listOf(
            Ingredient(
                name = "Tomato",
                type = IngredientType.Vegetable,
                quantity = 0.0,
                isAllergen = false,
                image = Image(label = "Tomato", url = "data:image/webp;base64,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")
            ),
            Ingredient(
                name = "Flour",
                type = IngredientType.BakingProducts,
                quantity = 0.0,
                isAllergen = false,
                image = Image(label = "Flour", url = "data:image/webp;base64,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")
            ),
            Ingredient(
                name = "Milk",
                type = IngredientType.MilkProducts,
                quantity = 0.0,
                isAllergen = true,
                image = Image(label = "Milk", url = "data:image/webp;base64,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")
            ),
            // Queda por agregar más ingredientes
        )

        val saveResult = repository.saveIngredients(initialIngredients)
        saveResult.onSuccess {
            println("Ingredientes iniciales guardados exitosamente")
        }.onFailure { error ->
            println("Error al guardar ingredientes iniciales: ${error.message}")
        }
    }

}
