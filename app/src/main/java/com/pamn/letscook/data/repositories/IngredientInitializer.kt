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
            // Vegetables
            Ingredient(name = "Tomato", type = IngredientType.Vegetable, quantity = 0.0, isAllergen = false, image = Image(label = "Tomato", url = "")),
            Ingredient(name = "Onion", type = IngredientType.Vegetable, quantity = 0.0, isAllergen = false, image = Image(label = "Onion", url = "")),
            Ingredient(name = "Garlic", type = IngredientType.Vegetable, quantity = 0.0, isAllergen = false, image = Image(label = "Garlic", url = "")),
            Ingredient(name = "Carrot", type = IngredientType.Vegetable, quantity = 0.0, isAllergen = false, image = Image(label = "Carrot", url = "")),
            Ingredient(name = "Bell Pepper", type = IngredientType.Vegetable, quantity = 0.0, isAllergen = false, image = Image(label = "Bell Pepper", url = "")),
            Ingredient(name = "Cucumber", type = IngredientType.Vegetable, quantity = 0.0, isAllergen = false, image = Image(label = "Cucumber", url = "")),
            Ingredient(name = "Spinach", type = IngredientType.Vegetable, quantity = 0.0, isAllergen = false, image = Image(label = "Spinach", url = "")),
            Ingredient(name = "Lettuce", type = IngredientType.Vegetable, quantity = 0.0, isAllergen = false, image = Image(label = "Lettuce", url = "")),
            Ingredient(name = "Potato", type = IngredientType.Vegetable, quantity = 0.0, isAllergen = false, image = Image(label = "Potato", url = "")),
            Ingredient(name = "Broccoli", type = IngredientType.Vegetable, quantity = 0.0, isAllergen = false, image = Image(label = "Broccoli", url = "")),
            Ingredient(name = "Zucchini", type = IngredientType.Vegetable, quantity = 0.0, isAllergen = false, image = Image(label = "Zucchini", url = "")),
            Ingredient(name = "Eggplant", type = IngredientType.Vegetable, quantity = 0.0, isAllergen = false, image = Image(label = "Eggplant", url = "")),
            Ingredient(name = "Celery", type = IngredientType.Vegetable, quantity = 0.0, isAllergen = false, image = Image(label = "Celery", url = "")),

            // Meats
            Ingredient(name = "Chicken Breast", type = IngredientType.Meat, quantity = 0.0, isAllergen = false, image = Image(label = "Chicken Breast", url = "")),
            Ingredient(name = "Ground Beef", type = IngredientType.Meat, quantity = 0.0, isAllergen = false, image = Image(label = "Ground Beef", url = "")),
            Ingredient(name = "Salmon", type = IngredientType.Meat, quantity = 0.0, isAllergen = false, image = Image(label = "Salmon", url = "")),
            Ingredient(name = "Pork Chop", type = IngredientType.Meat, quantity = 0.0, isAllergen = false, image = Image(label = "Pork Chop", url = "")),
            Ingredient(name = "Turkey", type = IngredientType.Meat, quantity = 0.0, isAllergen = false, image = Image(label = "Turkey", url = "")),
            Ingredient(name = "Shrimp", type = IngredientType.Meat, quantity = 0.0, isAllergen = false, image = Image(label = "Shrimp", url = "")),
            Ingredient(name = "Tuna", type = IngredientType.Meat, quantity = 0.0, isAllergen = false, image = Image(label = "Tuna", url = "")),
            Ingredient(name = "Lamb", type = IngredientType.Meat, quantity = 0.0, isAllergen = false, image = Image(label = "Lamb", url = "")),

            // Fruits
            Ingredient(name = "Apple", type = IngredientType.Fruit, quantity = 0.0, isAllergen = false, image = Image(label = "Apple", url = "")),
            Ingredient(name = "Banana", type = IngredientType.Fruit, quantity = 0.0, isAllergen = false, image = Image(label = "Banana", url = "")),
            Ingredient(name = "Lemon", type = IngredientType.Fruit, quantity = 0.0, isAllergen = false, image = Image(label = "Lemon", url = "")),
            Ingredient(name = "Orange", type = IngredientType.Fruit, quantity = 0.0, isAllergen = false, image = Image(label = "Orange", url = "")),
            Ingredient(name = "Strawberry", type = IngredientType.Fruit, quantity = 0.0, isAllergen = false, image = Image(label = "Strawberry", url = "")),
            Ingredient(name = "Blueberry", type = IngredientType.Fruit, quantity = 0.0, isAllergen = false, image = Image(label = "Blueberry", url = "")),
            Ingredient(name = "Mango", type = IngredientType.Fruit, quantity = 0.0, isAllergen = false, image = Image(label = "Mango", url = "")),
            Ingredient(name = "Pineapple", type = IngredientType.Fruit, quantity = 0.0, isAllergen = false, image = Image(label = "Pineapple", url = "")),

            // Dairy and Eggs
            Ingredient(name = "Milk", type = IngredientType.MilkProducts, quantity = 0.0, isAllergen = true, image = Image(label = "Milk", url = "")),
            Ingredient(name = "Eggs", type = IngredientType.MilkProducts, quantity = 0.0, isAllergen = true, image = Image(label = "Eggs", url = "")),
            Ingredient(name = "Cheese", type = IngredientType.MilkProducts, quantity = 0.0, isAllergen = true, image = Image(label = "Cheese", url = "")),
            Ingredient(name = "Yogurt", type = IngredientType.MilkProducts, quantity = 0.0, isAllergen = true, image = Image(label = "Yogurt", url = "")),
            Ingredient(name = "Cream", type = IngredientType.MilkProducts, quantity = 0.0, isAllergen = true, image = Image(label = "Cream", url = "")),
            Ingredient(name = "Butter", type = IngredientType.MilkProducts, quantity = 0.0, isAllergen = true, image = Image(label = "Butter", url = "")),

            // Baking and Pantry
            Ingredient(name = "Flour", type = IngredientType.BakingProducts, quantity = 0.0, isAllergen = false, image = Image(label = "Flour", url = "")),
            Ingredient(name = "Sugar", type = IngredientType.BakingProducts, quantity = 0.0, isAllergen = false, image = Image(label = "Sugar", url = "")),
            Ingredient(name = "Salt", type = IngredientType.BakingProducts, quantity = 0.0, isAllergen = false, image = Image(label = "Salt", url = "")),
            Ingredient(name = "Olive Oil", type = IngredientType.BakingProducts, quantity = 0.0, isAllergen = false, image = Image(label = "Olive Oil", url = "")),
            Ingredient(name = "Vegetable Oil", type = IngredientType.BakingProducts, quantity = 0.0, isAllergen = false, image = Image(label = "Vegetable Oil", url = "")),
            Ingredient(name = "Baking Powder", type = IngredientType.BakingProducts, quantity = 0.0, isAllergen = false, image = Image(label = "Baking Powder", url = "")),
            Ingredient(name = "Baking Soda", type = IngredientType.BakingProducts, quantity = 0.0, isAllergen = false, image = Image(label = "Baking Soda", url = "")),
            Ingredient(name = "Vanilla Extract", type = IngredientType.BakingProducts, quantity = 0.0, isAllergen = false, image = Image(label = "Vanilla Extract", url = "")),
            Ingredient(name = "Brown Sugar", type = IngredientType.BakingProducts, quantity = 0.0, isAllergen = false, image = Image(label = "Brown Sugar", url = "")),

            // Herbs and Spices
            Ingredient(name = "Black Pepper", type = IngredientType.Herbs, quantity = 0.0, isAllergen = false, image = Image(label = "Black Pepper", url = "")),
            Ingredient(name = "Basil", type = IngredientType.Herbs, quantity = 0.0, isAllergen = false, image = Image(label = "Basil", url = "")),
            Ingredient(name = "Oregano", type = IngredientType.Herbs, quantity = 0.0, isAllergen = false, image = Image(label = "Oregano", url = "")),
            Ingredient(name = "Thyme", type = IngredientType.Herbs, quantity = 0.0, isAllergen = false, image = Image(label = "Thyme", url = "")),
            Ingredient(name = "Rosemary", type = IngredientType.Herbs, quantity = 0.0, isAllergen = false, image = Image(label = "Rosemary", url = "")),
            Ingredient(name = "Cinnamon", type = IngredientType.Herbs, quantity = 0.0, isAllergen = false, image = Image(label = "Cinnamon", url = "")),
            Ingredient(name = "Paprika", type = IngredientType.Herbs, quantity = 0.0, isAllergen = false, image = Image(label = "Paprika", url = "")),
            Ingredient(name = "Cumin", type = IngredientType.Herbs, quantity = 0.0, isAllergen = false, image = Image(label = "Cumin", url = "")),

            // Grains and Legumes
            Ingredient(name = "Rice", type = IngredientType.BakingProducts, quantity = 0.0, isAllergen = false, image = Image(label = "Rice", url = "")),
            Ingredient(name = "Pasta", type = IngredientType.BakingProducts, quantity = 0.0, isAllergen = false, image = Image(label = "Pasta", url = "")),
            Ingredient(name = "Quinoa", type = IngredientType.BakingProducts, quantity = 0.0, isAllergen = false, image = Image(label = "Quinoa", url = "")),
            Ingredient(name = "Beans", type = IngredientType.BakingProducts, quantity = 0.0, isAllergen = false, image = Image(label = "Beans", url = "")),
            Ingredient(name = "Lentils", type = IngredientType.BakingProducts, quantity = 0.0, isAllergen = false, image = Image(label = "Lentils", url = ""))
        )

        val saveResult = repository.saveIngredients(initialIngredients)
        saveResult.onSuccess {
            println("Ingredientes iniciales guardados exitosamente")
        }.onFailure { error ->
            println("Error al guardar ingredientes iniciales: ${error.message}")
        }
    }

}
