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
            Ingredient(name = "Tomato", type = IngredientType.Vegetable, quantity = 0.0, isAllergen = false, image = Image(label = "Tomato", url = "https://cdn.pixabay.com/photo/2021/01/01/15/31/sushi-balls-5878892_960_720.jpg")),
            Ingredient(name = "Tomato", type = IngredientType.Vegetable, quantity = 0.0, isAllergen = false, image = Image(label = "Tomato", url = "https://cdn.pixabay.com/photo/2021/01/01/15/31/sushi-balls-5878892_960_720.jpg")),
            Ingredient(name = "Onion", type = IngredientType.Vegetable, quantity = 0.0, isAllergen = false, image = Image(label = "Onion", url = "https://cdn.pixabay.com/photo/2021/01/01/15/31/sushi-balls-5878892_960_720.jpg")),
            Ingredient(name = "Garlic", type = IngredientType.Vegetable, quantity = 0.0, isAllergen = false, image = Image(label = "Garlic", url = "https://cdn.pixabay.com/photo/2021/01/01/15/31/sushi-balls-5878892_960_720.jpg")),
            Ingredient(name = "Carrot", type = IngredientType.Vegetable, quantity = 0.0, isAllergen = false, image = Image(label = "Carrot", url = "https://cdn.pixabay.com/photo/2021/01/01/15/31/sushi-balls-5878892_960_720.jpg")),
            Ingredient(name = "Bell Pepper", type = IngredientType.Vegetable, quantity = 0.0, isAllergen = false, image = Image(label = "Bell Pepper", url = "https://cdn.pixabay.com/photo/2021/01/01/15/31/sushi-balls-5878892_960_720.jpg")),
            Ingredient(name = "Cucumber", type = IngredientType.Vegetable, quantity = 0.0, isAllergen = false, image = Image(label = "Cucumber", url = "https://cdn.pixabay.com/photo/2021/01/01/15/31/sushi-balls-5878892_960_720.jpg")),
            Ingredient(name = "Spinach", type = IngredientType.Vegetable, quantity = 0.0, isAllergen = false, image = Image(label = "Spinach", url = "https://cdn.pixabay.com/photo/2021/01/01/15/31/sushi-balls-5878892_960_720.jpg")),
            Ingredient(name = "Lettuce", type = IngredientType.Vegetable, quantity = 0.0, isAllergen = false, image = Image(label = "Lettuce", url = "https://cdn.pixabay.com/photo/2021/01/01/15/31/sushi-balls-5878892_960_720.jpg")),
            Ingredient(name = "Potato", type = IngredientType.Vegetable, quantity = 0.0, isAllergen = false, image = Image(label = "Potato", url = "https://cdn.pixabay.com/photo/2021/01/01/15/31/sushi-balls-5878892_960_720.jpg")),
            Ingredient(name = "Broccoli", type = IngredientType.Vegetable, quantity = 0.0, isAllergen = false, image = Image(label = "Broccoli", url = "https://cdn.pixabay.com/photo/2021/01/01/15/31/sushi-balls-5878892_960_720.jpg")),
            Ingredient(name = "Zucchini", type = IngredientType.Vegetable, quantity = 0.0, isAllergen = false, image = Image(label = "Zucchini", url = "https://cdn.pixabay.com/photo/2021/01/01/15/31/sushi-balls-5878892_960_720.jpg")),
            Ingredient(name = "Eggplant", type = IngredientType.Vegetable, quantity = 0.0, isAllergen = false, image = Image(label = "Eggplant", url = "https://cdn.pixabay.com/photo/2021/01/01/15/31/sushi-balls-5878892_960_720.jpg")),
            Ingredient(name = "Celery", type = IngredientType.Vegetable, quantity = 0.0, isAllergen = false, image = Image(label = "Celery", url = "https://cdn.pixabay.com/photo/2021/01/01/15/31/sushi-balls-5878892_960_720.jpg")),

            // Meats
            Ingredient(name = "Chicken Breast", type = IngredientType.Meat, quantity = 0.0, isAllergen = false, image = Image(label = "Chicken Breast", url = "https://cdn.pixabay.com/photo/2021/01/01/15/31/sushi-balls-5878892_960_720.jpg")),
            Ingredient(name = "Ground Beef", type = IngredientType.Meat, quantity = 0.0, isAllergen = false, image = Image(label = "Ground Beef", url = "https://cdn.pixabay.com/photo/2021/01/01/15/31/sushi-balls-5878892_960_720.jpg")),
            Ingredient(name = "Salmon", type = IngredientType.Meat, quantity = 0.0, isAllergen = false, image = Image(label = "Salmon", url = "https://cdn.pixabay.com/photo/2021/01/01/15/31/sushi-balls-5878892_960_720.jpg")),
            Ingredient(name = "Pork Chop", type = IngredientType.Meat, quantity = 0.0, isAllergen = false, image = Image(label = "Pork Chop", url = "https://cdn.pixabay.com/photo/2021/01/01/15/31/sushi-balls-5878892_960_720.jpg")),
            Ingredient(name = "Turkey", type = IngredientType.Meat, quantity = 0.0, isAllergen = false, image = Image(label = "Turkey", url = "https://cdn.pixabay.com/photo/2021/01/01/15/31/sushi-balls-5878892_960_720.jpg")),
            Ingredient(name = "Shrimp", type = IngredientType.Meat, quantity = 0.0, isAllergen = false, image = Image(label = "Shrimp", url = "https://cdn.pixabay.com/photo/2021/01/01/15/31/sushi-balls-5878892_960_720.jpg")),
            Ingredient(name = "Tuna", type = IngredientType.Meat, quantity = 0.0, isAllergen = false, image = Image(label = "Tuna", url = "https://cdn.pixabay.com/photo/2021/01/01/15/31/sushi-balls-5878892_960_720.jpg")),
            Ingredient(name = "Lamb", type = IngredientType.Meat, quantity = 0.0, isAllergen = false, image = Image(label = "Lamb", url = "https://cdn.pixabay.com/photo/2021/01/01/15/31/sushi-balls-5878892_960_720.jpg")),

            // Fruits
            Ingredient(name = "Apple", type = IngredientType.Fruit, quantity = 0.0, isAllergen = false, image = Image(label = "Apple", url = "https://cdn.pixabay.com/photo/2021/01/01/15/31/sushi-balls-5878892_960_720.jpg")),
            Ingredient(name = "Avocado", type = IngredientType.Fruit, quantity = 0.0, isAllergen = false, image = Image(label = "Avocado", url = "https://cdn.pixabay.com/photo/2021/01/01/15/31/sushi-balls-5878892_960_720.jpg")),
            Ingredient(name = "Banana", type = IngredientType.Fruit, quantity = 0.0, isAllergen = false, image = Image(label = "Banana", url = "https://cdn.pixabay.com/photo/2021/01/01/15/31/sushi-balls-5878892_960_720.jpg")),
            Ingredient(name = "Lemon", type = IngredientType.Fruit, quantity = 0.0, isAllergen = false, image = Image(label = "Lemon", url = "https://cdn.pixabay.com/photo/2021/01/01/15/31/sushi-balls-5878892_960_720.jpg")),
            Ingredient(name = "Orange", type = IngredientType.Fruit, quantity = 0.0, isAllergen = false, image = Image(label = "Orange", url = "https://cdn.pixabay.com/photo/2021/01/01/15/31/sushi-balls-5878892_960_720.jpg")),
            Ingredient(name = "Strawberry", type = IngredientType.Fruit, quantity = 0.0, isAllergen = false, image = Image(label = "Strawberry", url = "https://cdn.pixabay.com/photo/2021/01/01/15/31/sushi-balls-5878892_960_720.jpg")),
            Ingredient(name = "Blueberry", type = IngredientType.Fruit, quantity = 0.0, isAllergen = false, image = Image(label = "Blueberry", url = "https://cdn.pixabay.com/photo/2021/01/01/15/31/sushi-balls-5878892_960_720.jpg")),
            Ingredient(name = "Mango", type = IngredientType.Fruit, quantity = 0.0, isAllergen = false, image = Image(label = "Mango", url = "https://cdn.pixabay.com/photo/2021/01/01/15/31/sushi-balls-5878892_960_720.jpg")),
            Ingredient(name = "Pineapple", type = IngredientType.Fruit, quantity = 0.0, isAllergen = false, image = Image(label = "Pineapple", url = "https://cdn.pixabay.com/photo/2021/01/01/15/31/sushi-balls-5878892_960_720.jpg")),

            // Dairy and Eggs
            Ingredient(name = "Milk", type = IngredientType.MilkProducts, quantity = 0.0, isAllergen = true, image = Image(label = "Milk", url = "https://cdn.pixabay.com/photo/2021/01/01/15/31/sushi-balls-5878892_960_720.jpg")),
            Ingredient(name = "Eggs", type = IngredientType.MilkProducts, quantity = 0.0, isAllergen = true, image = Image(label = "Eggs", url = "https://cdn.pixabay.com/photo/2021/01/01/15/31/sushi-balls-5878892_960_720.jpg")),
            Ingredient(name = "Cheese", type = IngredientType.MilkProducts, quantity = 0.0, isAllergen = true, image = Image(label = "Cheese", url = "https://cdn.pixabay.com/photo/2021/01/01/15/31/sushi-balls-5878892_960_720.jpg")),
            Ingredient(name = "Yogurt", type = IngredientType.MilkProducts, quantity = 0.0, isAllergen = true, image = Image(label = "Yogurt", url = "https://cdn.pixabay.com/photo/2021/01/01/15/31/sushi-balls-5878892_960_720.jpg")),
            Ingredient(name = "Cream", type = IngredientType.MilkProducts, quantity = 0.0, isAllergen = true, image = Image(label = "Cream", url = "https://cdn.pixabay.com/photo/2021/01/01/15/31/sushi-balls-5878892_960_720.jpg")),
            Ingredient(name = "Butter", type = IngredientType.MilkProducts, quantity = 0.0, isAllergen = true, image = Image(label = "Butter", url = "https://cdn.pixabay.com/photo/2021/01/01/15/31/sushi-balls-5878892_960_720.jpg")),

            // Baking and Pantry
            Ingredient(name = "Flour", type = IngredientType.BakingProducts, quantity = 0.0, isAllergen = false, image = Image(label = "Flour", url = "https://cdn.pixabay.com/photo/2021/01/01/15/31/sushi-balls-5878892_960_720.jpg")),
            Ingredient(name = "Sugar", type = IngredientType.BakingProducts, quantity = 0.0, isAllergen = false, image = Image(label = "Sugar", url = "https://cdn.pixabay.com/photo/2021/01/01/15/31/sushi-balls-5878892_960_720.jpg")),
            Ingredient(name = "Salt", type = IngredientType.BakingProducts, quantity = 0.0, isAllergen = false, image = Image(label = "Salt", url = "https://cdn.pixabay.com/photo/2021/01/01/15/31/sushi-balls-5878892_960_720.jpg")),
            Ingredient(name = "Olive Oil", type = IngredientType.BakingProducts, quantity = 0.0, isAllergen = false, image = Image(label = "Olive Oil", url = "https://cdn.pixabay.com/photo/2021/01/01/15/31/sushi-balls-5878892_960_720.jpg")),
            Ingredient(name = "Vegetable Oil", type = IngredientType.BakingProducts, quantity = 0.0, isAllergen = false, image = Image(label = "Vegetable Oil", url = "https://cdn.pixabay.com/photo/2021/01/01/15/31/sushi-balls-5878892_960_720.jpg")),
            Ingredient(name = "Baking Powder", type = IngredientType.BakingProducts, quantity = 0.0, isAllergen = false, image = Image(label = "Baking Powder", url = "https://cdn.pixabay.com/photo/2021/01/01/15/31/sushi-balls-5878892_960_720.jpg")),
            Ingredient(name = "Baking Soda", type = IngredientType.BakingProducts, quantity = 0.0, isAllergen = false, image = Image(label = "Baking Soda", url = "https://cdn.pixabay.com/photo/2021/01/01/15/31/sushi-balls-5878892_960_720.jpg")),
            Ingredient(name = "Vanilla Extract", type = IngredientType.BakingProducts, quantity = 0.0, isAllergen = false, image = Image(label = "Vanilla Extract", url = "https://cdn.pixabay.com/photo/2021/01/01/15/31/sushi-balls-5878892_960_720.jpg")),
            Ingredient(name = "Brown Sugar", type = IngredientType.BakingProducts, quantity = 0.0, isAllergen = false, image = Image(label = "Brown Sugar", url = "https://cdn.pixabay.com/photo/2021/01/01/15/31/sushi-balls-5878892_960_720.jpg")),

            // Herbs and Spices
            Ingredient(name = "Black Pepper", type = IngredientType.Herbs, quantity = 0.0, isAllergen = false, image = Image(label = "Black Pepper", url = "https://cdn.pixabay.com/photo/2021/01/01/15/31/sushi-balls-5878892_960_720.jpg")),
            Ingredient(name = "Basil", type = IngredientType.Herbs, quantity = 0.0, isAllergen = false, image = Image(label = "Basil", url = "https://cdn.pixabay.com/photo/2021/01/01/15/31/sushi-balls-5878892_960_720.jpg")),
            Ingredient(name = "Oregano", type = IngredientType.Herbs, quantity = 0.0, isAllergen = false, image = Image(label = "Oregano", url = "https://cdn.pixabay.com/photo/2021/01/01/15/31/sushi-balls-5878892_960_720.jpg")),
            Ingredient(name = "Thyme", type = IngredientType.Herbs, quantity = 0.0, isAllergen = false, image = Image(label = "Thyme", url = "https://cdn.pixabay.com/photo/2021/01/01/15/31/sushi-balls-5878892_960_720.jpg")),
            Ingredient(name = "Rosemary", type = IngredientType.Herbs, quantity = 0.0, isAllergen = false, image = Image(label = "Rosemary", url = "https://cdn.pixabay.com/photo/2021/01/01/15/31/sushi-balls-5878892_960_720.jpg")),
            Ingredient(name = "Cinnamon", type = IngredientType.Herbs, quantity = 0.0, isAllergen = false, image = Image(label = "Cinnamon", url = "https://cdn.pixabay.com/photo/2021/01/01/15/31/sushi-balls-5878892_960_720.jpg")),
            Ingredient(name = "Paprika", type = IngredientType.Herbs, quantity = 0.0, isAllergen = false, image = Image(label = "Paprika", url = "https://cdn.pixabay.com/photo/2021/01/01/15/31/sushi-balls-5878892_960_720.jpg")),
            Ingredient(name = "Cumin", type = IngredientType.Herbs, quantity = 0.0, isAllergen = false, image = Image(label = "Cumin", url = "https://cdn.pixabay.com/photo/2021/01/01/15/31/sushi-balls-5878892_960_720.jpg")),

            // Grains and Legumes
            Ingredient(name = "Rice", type = IngredientType.BakingProducts, quantity = 0.0, isAllergen = false, image = Image(label = "Rice", url = "https://cdn.pixabay.com/photo/2021/01/01/15/31/sushi-balls-5878892_960_720.jpg")),
            Ingredient(name = "Pasta", type = IngredientType.BakingProducts, quantity = 0.0, isAllergen = false, image = Image(label = "Pasta", url = "https://cdn.pixabay.com/photo/2021/01/01/15/31/sushi-balls-5878892_960_720.jpg")),
            Ingredient(name = "Quinoa", type = IngredientType.BakingProducts, quantity = 0.0, isAllergen = false, image = Image(label = "Quinoa", url = "https://cdn.pixabay.com/photo/2021/01/01/15/31/sushi-balls-5878892_960_720.jpg")),
            Ingredient(name = "Beans", type = IngredientType.BakingProducts, quantity = 0.0, isAllergen = false, image = Image(label = "Beans", url = "https://cdn.pixabay.com/photo/2021/01/01/15/31/sushi-balls-5878892_960_720.jpg")),
            Ingredient(name = "Lentils", type = IngredientType.BakingProducts, quantity = 0.0, isAllergen = false, image = Image(label = "Lentils", url = "https://cdn.pixabay.com/photo/2021/01/01/15/31/sushi-balls-5878892_960_720.jpg")),

            // Baking and Pantry
            Ingredient(name = "Cocoa Powder", type = IngredientType.BakingProducts, quantity = 0.0, isAllergen = false, image = Image(label = "Cocoa Powder", url = "https://cdn.pixabay.com/photo/2021/01/01/15/31/sushi-balls-5878892_960_720.jpg")),
            Ingredient( name = "Maple Syrup", type = IngredientType.BakingProducts, quantity = 0.0, isAllergen = false, image = Image(label = "Maple Syrup", url = "https://cdn.pixabay.com/photo/2021/01/01/15/31/sushi-balls-5878892_960_720.jpg")),
            Ingredient( name = "Vanilla Extract", type = IngredientType.BakingProducts, quantity = 0.0, isAllergen = false, image = Image(label = "Vanilla Extract", url = "https://cdn.pixabay.com/photo/2021/01/01/15/31/sushi-balls-5878892_960_720.jpg")),

            )

        val saveResult = repository.saveIngredients(initialIngredients)
        saveResult.onSuccess {
            println("Ingredientes iniciales guardados exitosamente")
        }.onFailure { error ->
            println("Error al guardar ingredientes iniciales: ${error.message}")
        }
    }

}
