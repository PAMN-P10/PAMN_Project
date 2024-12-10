package com.pamn.letscook.domain.usecases

import com.pamn.letscook.data.repositories.IngredientRepository
import com.pamn.letscook.domain.models.Ingredient
import com.pamn.letscook.domain.models.Image
import com.pamn.letscook.domain.models.IngredientType

class IngredientInitializer(private val repository: IngredientRepository) {

    // Función para inicializar datos
    suspend fun initializeIngredientsIfEmpty() {
        // Primero, revisa si la colección ya tiene datos
        val result = repository.getAllIngredients()
        if (result.isSuccess && result.getOrThrow().isEmpty()) {
            // Lista de datos iniciales
            val initialIngredients = listOf(
                Ingredient(
                    name = "Tomato",
                    type = IngredientType.Vegetable,
                    quantity = 0.0,
                    isAllergen = false,
                    image = Image(label = "Tomato", url = "https://example.com/tomato.png")
                ),
                Ingredient(
                    name = "Flour",
                    type = IngredientType.BakingProducts,
                    quantity = 0.0,
                    isAllergen = false,
                    image = Image(label = "Flour", url = "https://example.com/flour.png")
                ),
                Ingredient(
                    name = "Milk",
                    type = IngredientType.MilkProducts,
                    quantity = 0.0,
                    isAllergen = true,
                    image = Image(label = "Milk", url = "https://example.com/milk.png")
                ),
                // Queda por agregar más ingredientes
            )

            // Inserta los ingredientes iniciales en Firestore
            repository.saveIngredients(initialIngredients)
        }
    }
}
