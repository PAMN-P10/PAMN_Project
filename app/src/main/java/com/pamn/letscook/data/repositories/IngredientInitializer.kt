package com.pamn.letscook.domain.usecases

import com.pamn.letscook.data.repositories.IngredientRepository
import com.pamn.letscook.domain.models.Ingredient
import com.pamn.letscook.domain.models.Image
import com.pamn.letscook.domain.models.IngredientType
import com.pamn.letscook.domain.models.MeasurementUnit

//  formato correcto para un enlace directo a un archivo en Google Drive
//https://drive.google.com/uc?export=view&id=FILE_ID

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
            Ingredient(name = "Tomato", type = IngredientType.Vegetable, quantity = 0.0, unit = MeasurementUnit.GRAMS, isAllergen = false, image = Image(label = "Tomato", url = "https://drive.google.com/uc?export=view&id=1OqmdIhDP4PQZbQGWEMSHfgbK7pW3h3Zu")),
            Ingredient(name = "Onion", type = IngredientType.Vegetable, quantity = 0.0, unit = MeasurementUnit.PIECES, isAllergen = false, image = Image(label = "Onion", url = "https://drive.google.com/uc?export=view&id=1OqmdIhDP4PQZbQGWEMSHfgbK7pW3h3Zu")),
            Ingredient(name = "Garlic", type = IngredientType.Vegetable, quantity = 0.0, unit = MeasurementUnit.PIECES, isAllergen = false, image = Image(label = "Garlic", url = "https://drive.google.com/uc?export=view&id=1OqmdIhDP4PQZbQGWEMSHfgbK7pW3h3Zu")),
            Ingredient(name = "Carrot", type = IngredientType.Vegetable, quantity = 0.0, unit = MeasurementUnit.PIECES, isAllergen = false, image = Image(label = "Carrot", url = "https://drive.google.com/uc?export=view&id=1OqmdIhDP4PQZbQGWEMSHfgbK7pW3h3Zu")),
            Ingredient(name = "Bell Pepper", type = IngredientType.Vegetable, quantity = 0.0, unit = MeasurementUnit.PIECES, isAllergen = false, image = Image(label = "Bell Pepper", url = "https://drive.google.com/uc?export=view&id=1OqmdIhDP4PQZbQGWEMSHfgbK7pW3h3Zu")),
            Ingredient(name = "Cucumber", type = IngredientType.Vegetable, quantity = 0.0, unit = MeasurementUnit.PIECES, isAllergen = false, image = Image(label = "Cucumber", url = "https://drive.google.com/uc?export=view&id=1OqmdIhDP4PQZbQGWEMSHfgbK7pW3h3Zu")),
            Ingredient(name = "Spinach", type = IngredientType.Vegetable, quantity = 0.0, unit = MeasurementUnit.GRAMS, isAllergen = false, image = Image(label = "Spinach", url = "https://drive.google.com/uc?export=view&id=1OqmdIhDP4PQZbQGWEMSHfgbK7pW3h3Zu")),
            Ingredient(name = "Lettuce", type = IngredientType.Vegetable, quantity = 0.0, unit = MeasurementUnit.GRAMS, isAllergen = false, image = Image(label = "Lettuce", url = "https://drive.google.com/uc?export=view&id=1OqmdIhDP4PQZbQGWEMSHfgbK7pW3h3Zu")),
            Ingredient(name = "Potato", type = IngredientType.Vegetable, quantity = 0.0, unit = MeasurementUnit.GRAMS, isAllergen = false, image = Image(label = "Potato", url = "https://drive.google.com/uc?export=view&id=1OqmdIhDP4PQZbQGWEMSHfgbK7pW3h3Zu")),
            Ingredient(name = "Broccoli", type = IngredientType.Vegetable, quantity = 0.0, unit = MeasurementUnit.GRAMS, isAllergen = false, image = Image(label = "Broccoli", url = "https://drive.google.com/uc?export=view&id=1OqmdIhDP4PQZbQGWEMSHfgbK7pW3h3Zu")),
            Ingredient(name = "Zucchini", type = IngredientType.Vegetable, quantity = 0.0, unit = MeasurementUnit.PIECES, isAllergen = false, image = Image(label = "Zucchini", url = "https://drive.google.com/uc?export=view&id=1OqmdIhDP4PQZbQGWEMSHfgbK7pW3h3Zu")),
            Ingredient(name = "Eggplant", type = IngredientType.Vegetable, quantity = 0.0, unit = MeasurementUnit.PIECES, isAllergen = false, image = Image(label = "Eggplant", url = "https://drive.google.com/uc?export=view&id=1OqmdIhDP4PQZbQGWEMSHfgbK7pW3h3Zu")),
            Ingredient(name = "Celery", type = IngredientType.Vegetable, quantity = 0.0, unit = MeasurementUnit.PIECES, isAllergen = false, image = Image(label = "Celery", url = "https://drive.google.com/uc?export=view&id=1OqmdIhDP4PQZbQGWEMSHfgbK7pW3h3Zu")),

            // Meats
            Ingredient(name = "Chicken Breast", type = IngredientType.Meat, quantity = 0.0, unit = MeasurementUnit.GRAMS, isAllergen = false, image = Image(label = "Chicken Breast", url = "https://drive.google.com/uc?export=view&id=1OqmdIhDP4PQZbQGWEMSHfgbK7pW3h3Zu")),
            Ingredient(name = "Ground Beef", type = IngredientType.Meat, quantity = 0.0, unit = MeasurementUnit.GRAMS, isAllergen = false, image = Image(label = "Ground Beef", url = "https://drive.google.com/uc?export=view&id=1OqmdIhDP4PQZbQGWEMSHfgbK7pW3h3Zu")),
            Ingredient(name = "Salmon", type = IngredientType.Meat, quantity = 0.0, unit = MeasurementUnit.GRAMS, isAllergen = false, image = Image(label = "Salmon", url = "https://drive.google.com/uc?export=view&id=1OqmdIhDP4PQZbQGWEMSHfgbK7pW3h3Zu")),
            Ingredient(name = "Pork Chop", type = IngredientType.Meat, quantity = 0.0, unit = MeasurementUnit.GRAMS, isAllergen = false, image = Image(label = "Pork Chop", url = "https://drive.google.com/uc?export=view&id=1OqmdIhDP4PQZbQGWEMSHfgbK7pW3h3Zu")),
            Ingredient(name = "Turkey", type = IngredientType.Meat, quantity = 0.0, unit = MeasurementUnit.GRAMS, isAllergen = false, image = Image(label = "Turkey", url = "https://drive.google.com/uc?export=view&id=1OqmdIhDP4PQZbQGWEMSHfgbK7pW3h3Zu")),
            Ingredient(name = "Shrimp", type = IngredientType.Meat, quantity = 0.0, unit = MeasurementUnit.GRAMS, isAllergen = false, image = Image(label = "Shrimp", url = "https://drive.google.com/uc?export=view&id=1OqmdIhDP4PQZbQGWEMSHfgbK7pW3h3Zu")),
            Ingredient(name = "Tuna", type = IngredientType.Meat, quantity = 0.0, unit = MeasurementUnit.GRAMS, isAllergen = false, image = Image(label = "Tuna", url = "https://drive.google.com/uc?export=view&id=1OqmdIhDP4PQZbQGWEMSHfgbK7pW3h3Zu")),
            Ingredient(name = "Lamb", type = IngredientType.Meat, quantity = 0.0, unit = MeasurementUnit.GRAMS, isAllergen = false, image = Image(label = "Lamb", url = "https://drive.google.com/uc?export=view&id=1OqmdIhDP4PQZbQGWEMSHfgbK7pW3h3Zu")),

            // Fruits
            Ingredient(name = "Apple", type = IngredientType.Fruit, quantity = 0.0, isAllergen = false, image = Image(label = "Apple", url = "https://drive.google.com/uc?export=view&id=1OqmdIhDP4PQZbQGWEMSHfgbK7pW3h3Zu")),
            Ingredient(name = "Avocado", type = IngredientType.Fruit, quantity = 0.0, isAllergen = false, image = Image(label = "Avocado", url = "https://drive.google.com/uc?export=view&id=1OqmdIhDP4PQZbQGWEMSHfgbK7pW3h3Zu")),
            Ingredient(name = "Banana", type = IngredientType.Fruit, quantity = 0.0, isAllergen = false, image = Image(label = "Banana", url = "https://drive.google.com/uc?export=view&id=1OqmdIhDP4PQZbQGWEMSHfgbK7pW3h3Zu")),
            Ingredient(name = "Lemon", type = IngredientType.Fruit, quantity = 0.0, isAllergen = false, image = Image(label = "Lemon", url = "https://drive.google.com/uc?export=view&id=1OqmdIhDP4PQZbQGWEMSHfgbK7pW3h3Zu")),
            Ingredient(name = "Orange", type = IngredientType.Fruit, quantity = 0.0, isAllergen = false, image = Image(label = "Orange", url = "https://drive.google.com/uc?export=view&id=1OqmdIhDP4PQZbQGWEMSHfgbK7pW3h3Zu")),
            Ingredient(name = "Strawberry", type = IngredientType.Fruit, quantity = 0.0, isAllergen = false, image = Image(label = "Strawberry", url = "https://drive.google.com/uc?export=view&id=1OqmdIhDP4PQZbQGWEMSHfgbK7pW3h3Zu")),
            Ingredient(name = "Blueberry", type = IngredientType.Fruit, quantity = 0.0, isAllergen = false, image = Image(label = "Blueberry", url = "https://drive.google.com/uc?export=view&id=1OqmdIhDP4PQZbQGWEMSHfgbK7pW3h3Zu")),
            Ingredient(name = "Mango", type = IngredientType.Fruit, quantity = 0.0, isAllergen = false, image = Image(label = "Mango", url = "https://drive.google.com/uc?export=view&id=1OqmdIhDP4PQZbQGWEMSHfgbK7pW3h3Zu")),
            Ingredient(name = "Pineapple", type = IngredientType.Fruit, quantity = 0.0, isAllergen = false, image = Image(label = "Pineapple", url = "https://drive.google.com/uc?export=view&id=1OqmdIhDP4PQZbQGWEMSHfgbK7pW3h3Zu")),

            // Dairy and Eggs
            Ingredient(name = "Milk", type = IngredientType.MilkProducts, quantity = 0.0, unit = MeasurementUnit.MILLILITERS, isAllergen = true, image = Image(label = "Milk", url = "https://drive.google.com/uc?export=view&id=1OqmdIhDP4PQZbQGWEMSHfgbK7pW3h3Zu")),
            Ingredient(name = "Eggs", type = IngredientType.MilkProducts, quantity = 0.0, unit = MeasurementUnit.PIECES, isAllergen = true, image = Image(label = "Eggs", url = "https://drive.google.com/uc?export=view&id=1OqmdIhDP4PQZbQGWEMSHfgbK7pW3h3Zu")),
            Ingredient(name = "Cheese", type = IngredientType.MilkProducts, quantity = 0.0, unit = MeasurementUnit.GRAMS, isAllergen = true, image = Image(label = "Cheese", url = "https://drive.google.com/uc?export=view&id=1OqmdIhDP4PQZbQGWEMSHfgbK7pW3h3Zu")),
            Ingredient(name = "Yogurt", type = IngredientType.MilkProducts, quantity = 0.0, unit = MeasurementUnit.GRAMS, isAllergen = true, image = Image(label = "Yogurt", url = "https://drive.google.com/uc?export=view&id=1OqmdIhDP4PQZbQGWEMSHfgbK7pW3h3Zu")),
            Ingredient(name = "Cream", type = IngredientType.MilkProducts, quantity = 0.0, unit = MeasurementUnit.MILLILITERS, isAllergen = true, image = Image(label = "Cream", url = "https://drive.google.com/uc?export=view&id=1OqmdIhDP4PQZbQGWEMSHfgbK7pW3h3Zu")),
            Ingredient(name = "Butter", type = IngredientType.MilkProducts, quantity = 0.0, unit = MeasurementUnit.GRAMS, isAllergen = true, image = Image(label = "Butter", url = "https://drive.google.com/uc?export=view&id=1OqmdIhDP4PQZbQGWEMSHfgbK7pW3h3Zu")),

            // Baking and Pantry
            Ingredient(name = "Flour", type = IngredientType.BakingProducts, quantity = 0.0, unit = MeasurementUnit.GRAMS, isAllergen = false, image = Image(label = "Flour", url = "https://drive.google.com/uc?export=view&id=1OqmdIhDP4PQZbQGWEMSHfgbK7pW3h3Zu")),
            Ingredient(name = "Sugar", type = IngredientType.BakingProducts, quantity = 0.0, unit = MeasurementUnit.GRAMS, isAllergen = false, image = Image(label = "Sugar", url = "https://drive.google.com/uc?export=view&id=1OqmdIhDP4PQZbQGWEMSHfgbK7pW3h3Zu")),
            Ingredient(name = "Salt", type = IngredientType.BakingProducts, quantity = 0.0, unit = MeasurementUnit.GRAMS, isAllergen = false, image = Image(label = "Salt", url = "https://drive.google.com/uc?export=view&id=1OqmdIhDP4PQZbQGWEMSHfgbK7pW3h3Zu")),
            Ingredient(name = "Olive Oil", type = IngredientType.BakingProducts, quantity = 0.0, unit = MeasurementUnit.MILLILITERS, isAllergen = false, image = Image(label = "Olive Oil", url = "https://drive.google.com/uc?export=view&id=1OqmdIhDP4PQZbQGWEMSHfgbK7pW3h3Zu")),
            Ingredient(name = "Vegetable Oil", type = IngredientType.BakingProducts, quantity = 0.0, unit = MeasurementUnit.MILLILITERS, isAllergen = false, image = Image(label = "Vegetable Oil", url = "https://drive.google.com/uc?export=view&id=1OqmdIhDP4PQZbQGWEMSHfgbK7pW3h3Zu")),
            Ingredient(name = "Baking Powder", type = IngredientType.BakingProducts, quantity = 0.0, unit = MeasurementUnit.GRAMS, isAllergen = false, image = Image(label = "Baking Powder", url = "https://drive.google.com/uc?export=view&id=1OqmdIhDP4PQZbQGWEMSHfgbK7pW3h3Zu")),
            Ingredient(name = "Baking Soda", type = IngredientType.BakingProducts, quantity = 0.0, unit = MeasurementUnit.GRAMS, isAllergen = false, image = Image(label = "Baking Soda", url = "https://drive.google.com/uc?export=view&id=1OqmdIhDP4PQZbQGWEMSHfgbK7pW3h3Zu")),
            Ingredient(name = "Vanilla Extract", type = IngredientType.BakingProducts, quantity = 0.0, unit = MeasurementUnit.MILLILITERS, isAllergen = false, image = Image(label = "Vanilla Extract", url = "https://drive.google.com/uc?export=view&id=1OqmdIhDP4PQZbQGWEMSHfgbK7pW3h3Zu")),
            Ingredient(name = "Brown Sugar", type = IngredientType.BakingProducts, quantity = 0.0, unit = MeasurementUnit.GRAMS, isAllergen = false, image = Image(label = "Brown Sugar", url = "https://drive.google.com/uc?export=view&id=1OqmdIhDP4PQZbQGWEMSHfgbK7pW3h3Zu")),

            // Herbs and Spices
            Ingredient(name = "Black Pepper", type = IngredientType.Herbs, quantity = 0.0, unit = MeasurementUnit.GRAMS, isAllergen = false, image = Image(label = "Black Pepper", url = "https://drive.google.com/uc?export=view&id=1OqmdIhDP4PQZbQGWEMSHfgbK7pW3h3Zu")),
            Ingredient(name = "Basil", type = IngredientType.Herbs, quantity = 0.0, unit = MeasurementUnit.GRAMS, isAllergen = false, image = Image(label = "Basil", url = "https://drive.google.com/uc?export=view&id=1OqmdIhDP4PQZbQGWEMSHfgbK7pW3h3Zu")),
            Ingredient(name = "Oregano", type = IngredientType.Herbs, quantity = 0.0, unit = MeasurementUnit.GRAMS, isAllergen = false, image = Image(label = "Oregano", url = "https://drive.google.com/uc?export=view&id=1OqmdIhDP4PQZbQGWEMSHfgbK7pW3h3Zu")),
            Ingredient(name = "Thyme", type = IngredientType.Herbs, quantity = 0.0, unit = MeasurementUnit.GRAMS, isAllergen = false, image = Image(label = "Thyme", url = "https://drive.google.com/uc?export=view&id=1OqmdIhDP4PQZbQGWEMSHfgbK7pW3h3Zu")),
            Ingredient(name = "Rosemary", type = IngredientType.Herbs, quantity = 0.0, unit = MeasurementUnit.GRAMS, isAllergen = false, image = Image(label = "Rosemary", url = "https://drive.google.com/uc?export=view&id=1OqmdIhDP4PQZbQGWEMSHfgbK7pW3h3Zu")),
            Ingredient(name = "Cinnamon", type = IngredientType.Herbs, quantity = 0.0, unit = MeasurementUnit.GRAMS, isAllergen = false, image = Image(label = "Cinnamon", url = "https://drive.google.com/uc?export=view&id=1OqmdIhDP4PQZbQGWEMSHfgbK7pW3h3Zu")),
            Ingredient(name = "Paprika", type = IngredientType.Herbs, quantity = 0.0, unit = MeasurementUnit.GRAMS, isAllergen = false, image = Image(label = "Paprika", url = "https://drive.google.com/uc?export=view&id=1OqmdIhDP4PQZbQGWEMSHfgbK7pW3h3Zu")),
            Ingredient(name = "Cumin", type = IngredientType.Herbs, quantity = 0.0, unit = MeasurementUnit.GRAMS, isAllergen = false, image = Image(label = "Cumin", url = "https://drive.google.com/uc?export=view&id=1OqmdIhDP4PQZbQGWEMSHfgbK7pW3h3Zu")),

            // Grains and Legumes
            Ingredient(name = "Rice", type = IngredientType.BakingProducts, quantity = 0.0, unit = MeasurementUnit.GRAMS, isAllergen = false, image = Image(label = "Rice", url = "https://drive.google.com/uc?export=view&id=1OqmdIhDP4PQZbQGWEMSHfgbK7pW3h3Zu")),
            Ingredient(name = "Pasta", type = IngredientType.BakingProducts, quantity = 0.0, unit = MeasurementUnit.GRAMS, isAllergen = false, image = Image(label = "Pasta", url = "https://drive.google.com/uc?export=view&id=1OqmdIhDP4PQZbQGWEMSHfgbK7pW3h3Zu")),
            Ingredient(name = "Quinoa", type = IngredientType.BakingProducts, quantity = 0.0, unit = MeasurementUnit.GRAMS, isAllergen = false, image = Image(label = "Quinoa", url = "https://drive.google.com/uc?export=view&id=1OqmdIhDP4PQZbQGWEMSHfgbK7pW3h3Zu")),
            Ingredient(name = "Beans", type = IngredientType.BakingProducts, quantity = 0.0, unit = MeasurementUnit.GRAMS, isAllergen = false, image = Image(label = "Beans", url = "https://drive.google.com/uc?export=view&id=1OqmdIhDP4PQZbQGWEMSHfgbK7pW3h3Zu")),
            Ingredient(name = "Lentils", type = IngredientType.BakingProducts, quantity = 0.0, unit = MeasurementUnit.GRAMS, isAllergen = false, image = Image(label = "Lentils", url = "https://drive.google.com/uc?export=view&id=1OqmdIhDP4PQZbQGWEMSHfgbK7pW3h3Zu")),

            // Baking and Pantry
            Ingredient(name = "Cocoa Powder", type = IngredientType.BakingProducts, quantity = 0.0, unit = MeasurementUnit.GRAMS, isAllergen = false, image = Image(label = "Cocoa Powder", url = "https://drive.google.com/uc?export=view&id=1OqmdIhDP4PQZbQGWEMSHfgbK7pW3h3Zu")),
            Ingredient(name = "Maple Syrup", type = IngredientType.BakingProducts, quantity = 0.0, unit = MeasurementUnit.MILLILITERS, isAllergen = false, image = Image(label = "Maple Syrup", url = "https://drive.google.com/uc?export=view&id=1OqmdIhDP4PQZbQGWEMSHfgbK7pW3h3Zu")),
            Ingredient(name = "Vanilla Extract", type = IngredientType.BakingProducts, quantity = 0.0, unit = MeasurementUnit.MILLILITERS, isAllergen = false, image = Image(label = "Vanilla Extract", url = "https://drive.google.com/uc?export=view&id=1OqmdIhDP4PQZbQGWEMSHfgbK7pW3h3Zu"))
            )

        val saveResult = repository.saveIngredients(initialIngredients)
        saveResult.onSuccess {
            println("Ingredientes iniciales guardados exitosamente")
        }.onFailure { error ->
            println("Error al guardar ingredientes iniciales: ${error.message}")
        }
    }

}
