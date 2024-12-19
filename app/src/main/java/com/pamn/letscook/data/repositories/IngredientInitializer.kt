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
            Ingredient(name = "Tomato", type = IngredientType.Vegetable, quantity = 0.0, unit = MeasurementUnit.GRAMS, isAllergen = false, image = Image(label = "Tomato", url = "https://cdn-icons-png.freepik.com/256/5635/5635776.png?ga=GA1.1.1446863664.1727893023&semt=ais_hybrid")),
            Ingredient(name = "Onion", type = IngredientType.Vegetable, quantity = 0.0, unit = MeasurementUnit.PIECES, isAllergen = false, image = Image(label = "Onion", url = "https://cdn-icons-png.freepik.com/256/5409/5409120.png?ga=GA1.1.1446863664.1727893023&semt=ais_hybrid")),
            Ingredient(name = "Garlic", type = IngredientType.Vegetable, quantity = 0.0, unit = MeasurementUnit.PIECES, isAllergen = false, image = Image(label = "Garlic", url = "https://cdn-icons-png.freepik.com/256/8967/8967574.png?ga=GA1.1.1446863664.1727893023&semt=ais_hybrid")),
            Ingredient(name = "Carrot", type = IngredientType.Vegetable, quantity = 0.0, unit = MeasurementUnit.PIECES, isAllergen = false, image = Image(label = "Carrot", url = "https://cdn-icons-png.freepik.com/256/1414/1414599.png?ga=GA1.1.1446863664.1727893023&semt=ais_hybrid")),
            Ingredient(name = "Bell Pepper", type = IngredientType.Vegetable, quantity = 0.0, unit = MeasurementUnit.PIECES, isAllergen = false, image = Image(label = "Bell Pepper", url = "https://cdn-icons-png.freepik.com/256/14066/14066780.png?ga=GA1.1.1446863664.1727893023&semt=ais_hybrid")),
            Ingredient(name = "Cucumber", type = IngredientType.Vegetable, quantity = 0.0, unit = MeasurementUnit.PIECES, isAllergen = false, image = Image(label = "Cucumber", url = "https://cdn-icons-png.freepik.com/256/8987/8987866.png?ga=GA1.1.1446863664.1727893023&semt=ais_hybrid")),
            Ingredient(name = "Spinach", type = IngredientType.Vegetable, quantity = 0.0, unit = MeasurementUnit.GRAMS, isAllergen = false, image = Image(label = "Spinach", url = "https://cdn-icons-png.freepik.com/256/8885/8885478.png?ga=GA1.1.1446863664.1727893023&semt=ais_hybrid")),
            Ingredient(name = "Lettuce", type = IngredientType.Vegetable, quantity = 0.0, unit = MeasurementUnit.GRAMS, isAllergen = false, image = Image(label = "Lettuce", url = "https://cdn-icons-png.freepik.com/256/15804/15804959.png?ga=GA1.1.1446863664.1727893023&semt=ais_hybrid")),
            Ingredient(name = "Potato", type = IngredientType.Vegetable, quantity = 0.0, unit = MeasurementUnit.GRAMS, isAllergen = false, image = Image(label = "Potato", url = "https://cdn-icons-png.freepik.com/256/14810/14810821.png?ga=GA1.1.1446863664.1727893023&semt=ais_hybrid")),
            Ingredient(name = "Broccoli", type = IngredientType.Vegetable, quantity = 0.0, unit = MeasurementUnit.GRAMS, isAllergen = false, image = Image(label = "Broccoli", url = "https://cdn-icons-png.freepik.com/256/8611/8611746.png?ga=GA1.1.1446863664.1727893023&semt=ais_hybrid")),
            Ingredient(name = "Zucchini", type = IngredientType.Vegetable, quantity = 0.0, unit = MeasurementUnit.PIECES, isAllergen = false, image = Image(label = "Zucchini", url = "https://cdn-icons-png.freepik.com/256/1589/1589811.png?ga=GA1.1.1446863664.1727893023&semt=ais_hybrid")),
            Ingredient(name = "Eggplant", type = IngredientType.Vegetable, quantity = 0.0, unit = MeasurementUnit.PIECES, isAllergen = false, image = Image(label = "Eggplant", url = "https://cdn-icons-png.freepik.com/256/11682/11682817.png?ga=GA1.1.1446863664.1727893023&semt=ais_hybrid")),

            // Meats
            Ingredient(name = "Chicken Breast", type = IngredientType.Meat, quantity = 0.0, unit = MeasurementUnit.GRAMS, isAllergen = false, image = Image(label = "Chicken Breast", url = "https://cdn-icons-png.freepik.com/256/5450/5450838.png?ga=GA1.1.1446863664.1727893023&semt=ais_hybrid")),
            Ingredient(name = "Ground Beef", type = IngredientType.Meat, quantity = 0.0, unit = MeasurementUnit.GRAMS, isAllergen = false, image = Image(label = "Ground Beef", url = "https://cdn-icons-png.freepik.com/256/1005/1005469.png?ga=GA1.1.1446863664.1727893023&semt=ais_hybrid")),
            Ingredient(name = "Salmon", type = IngredientType.Meat, quantity = 0.0, unit = MeasurementUnit.GRAMS, isAllergen = false, image = Image(label = "Salmon", url = "https://cdn-icons-png.freepik.com/256/6081/6081787.png?ga=GA1.1.1446863664.1727893023&semt=ais_hybrid")),
            Ingredient(name = "Pork Chop", type = IngredientType.Meat, quantity = 0.0, unit = MeasurementUnit.GRAMS, isAllergen = false, image = Image(label = "Pork Chop", url = "https://cdn-icons-png.freepik.com/256/1301/1301926.png?ga=GA1.1.1446863664.1727893023&semt=ais_hybrid")),
            Ingredient(name = "Turkey", type = IngredientType.Meat, quantity = 0.0, unit = MeasurementUnit.GRAMS, isAllergen = false, image = Image(label = "Turkey", url = "https://cdn-icons-png.freepik.com/256/10589/10589265.png?ga=GA1.1.1446863664.1727893023&semt=ais_hybrid")),
            Ingredient(name = "Shrimp", type = IngredientType.Meat, quantity = 0.0, unit = MeasurementUnit.GRAMS, isAllergen = false, image = Image(label = "Shrimp", url = "https://cdn-icons-png.freepik.com/256/9226/9226048.png?ga=GA1.1.1446863664.1727893023&semt=ais_hybrid")),
            Ingredient(name = "Tuna", type = IngredientType.Meat, quantity = 0.0, unit = MeasurementUnit.GRAMS, isAllergen = false, image = Image(label = "Tuna", url = "https://cdn-icons-png.freepik.com/256/539/539903.png?ga=GA1.1.1446863664.1727893023&semt=ais_hybrid")),
            Ingredient(name = "Lamb", type = IngredientType.Meat, quantity = 0.0, unit = MeasurementUnit.GRAMS, isAllergen = false, image = Image(label = "Lamb", url = "https://cdn-icons-png.freepik.com/256/9287/9287261.png?ga=GA1.1.1446863664.1727893023&semt=ais_hybrid")),

            // Fruits
            Ingredient(name = "Apple", type = IngredientType.Fruit, quantity = 0.0, isAllergen = false, image = Image(label = "Apple", url = "https://cdn-icons-png.freepik.com/256/14066/14066721.png?ga=GA1.1.1446863664.1727893023&semt=ais_hybrid")),
            Ingredient(name = "Avocado", type = IngredientType.Fruit, quantity = 0.0, isAllergen = false, image = Image(label = "Avocado", url = "https://cdn-icons-png.freepik.com/256/8165/8165171.png?ga=GA1.1.1446863664.1727893023&semt=ais_hybrid")),
            Ingredient(name = "Banana", type = IngredientType.Fruit, quantity = 0.0, isAllergen = false, image = Image(label = "Banana", url = "https://cdn-icons-png.freepik.com/256/9588/9588916.png?ga=GA1.1.1446863664.1727893023&semt=ais_hybrid")),
            Ingredient(name = "Lemon", type = IngredientType.Fruit, quantity = 0.0, isAllergen = false, image = Image(label = "Lemon", url = "https://cdn-icons-png.freepik.com/256/10940/10940803.png?ga=GA1.1.1446863664.1727893023&semt=ais_hybrid")),
            Ingredient(name = "Orange", type = IngredientType.Fruit, quantity = 0.0, isAllergen = false, image = Image(label = "Orange", url = "https://www.freepik.com/icon/orange_1228644#fromView=search&page=1&position=12&uuid=fbc7e929-7ee2-4730-83bf-cd03841a3d53")),
            Ingredient(name = "Strawberry", type = IngredientType.Fruit, quantity = 0.0, isAllergen = false, image = Image(label = "Strawberry", url = "https://cdn-icons-png.freepik.com/256/16065/16065660.png?ga=GA1.1.1446863664.1727893023&semt=ais_hybrid")),
            Ingredient(name = "Blueberry", type = IngredientType.Fruit, quantity = 0.0, isAllergen = false, image = Image(label = "Blueberry", url = "https://cdn-icons-png.freepik.com/256/14066/14066734.png?ga=GA1.1.1446863664.1727893023&semt=ais_hybrid")),
            Ingredient(name = "Mango", type = IngredientType.Fruit, quantity = 0.0, isAllergen = false, image = Image(label = "Mango", url = "https://cdn-icons-png.freepik.com/256/15155/15155223.png?ga=GA1.1.1446863664.1727893023&semt=ais_hybrid")),
            Ingredient(name = "Pineapple", type = IngredientType.Fruit, quantity = 0.0, isAllergen = false, image = Image(label = "Pineapple", url = "https://cdn-icons-png.freepik.com/256/693/693481.png?ga=GA1.1.1446863664.1727893023&semt=ais_hybrid")),

            // Dairy and Eggs
            Ingredient(name = "Milk", type = IngredientType.MilkProducts, quantity = 0.0, unit = MeasurementUnit.MILLILITERS, isAllergen = true, image = Image(label = "Milk", url = "https://cdn-icons-png.freepik.com/256/5125/5125422.png?ga=GA1.1.1446863664.1727893023&semt=ais_hybrid")),
            Ingredient(name = "Eggs", type = IngredientType.MilkProducts, quantity = 0.0, unit = MeasurementUnit.PIECES, isAllergen = true, image = Image(label = "Eggs", url = "https://cdn-icons-png.freepik.com/256/7075/7075910.png?ga=GA1.1.1446863664.1727893023&semt=ais_hybrid")),
            Ingredient(name = "Cheese", type = IngredientType.MilkProducts, quantity = 0.0, unit = MeasurementUnit.GRAMS, isAllergen = true, image = Image(label = "Cheese", url = "https://cdn-icons-png.freepik.com/256/601/601469.png?ga=GA1.1.1446863664.1727893023&semt=ais_hybrid")),
            Ingredient(name = "Yogurt", type = IngredientType.MilkProducts, quantity = 0.0, unit = MeasurementUnit.GRAMS, isAllergen = true, image = Image(label = "Yogurt", url = "https://cdn-icons-png.freepik.com/256/695/695353.png?ga=GA1.1.1446863664.1727893023&semt=ais_hybrid")),
            Ingredient(name = "Cream", type = IngredientType.MilkProducts, quantity = 0.0, unit = MeasurementUnit.MILLILITERS, isAllergen = true, image = Image(label = "Cream", url = "https://cdn-icons-png.freepik.com/256/10462/10462279.png?ga=GA1.1.1446863664.1727893023&semt=ais_hybrid")),
            Ingredient(name = "Butter", type = IngredientType.MilkProducts, quantity = 0.0, unit = MeasurementUnit.GRAMS, isAllergen = true, image = Image(label = "Butter", url = "https://cdn-icons-png.freepik.com/256/17828/17828741.png?ga=GA1.1.1446863664.1727893023&semt=ais_hybrid")),

            // Baking and Pantry
            Ingredient(name = "Flour", type = IngredientType.BakingProducts, quantity = 0.0, unit = MeasurementUnit.GRAMS, isAllergen = false, image = Image(label = "Flour", url = "https://cdn-icons-png.freepik.com/256/5029/5029282.png?ga=GA1.1.1446863664.1727893023&semt=ais_hybrid")),
            Ingredient(name = "Sugar", type = IngredientType.BakingProducts, quantity = 0.0, unit = MeasurementUnit.GRAMS, isAllergen = false, image = Image(label = "Sugar", url = "https://cdn-icons-png.freepik.com/256/10552/10552057.png?ga=GA1.1.1446863664.1727893023&semt=ais_hybrid")),
            Ingredient(name = "Salt", type = IngredientType.BakingProducts, quantity = 0.0, unit = MeasurementUnit.GRAMS, isAllergen = false, image = Image(label = "Salt", url = "https://cdn-icons-png.freepik.com/256/8080/8080408.png?ga=GA1.1.1446863664.1727893023&semt=ais_hybrid")),
            Ingredient(name = "Olive Oil", type = IngredientType.BakingProducts, quantity = 0.0, unit = MeasurementUnit.MILLILITERS, isAllergen = false, image = Image(label = "Olive Oil", url = "https://cdn-icons-png.freepik.com/256/4264/4264676.png?ga=GA1.1.1446863664.1727893023&semt=ais_hybrid")),
            Ingredient(name = "Vegetable Oil", type = IngredientType.BakingProducts, quantity = 0.0, unit = MeasurementUnit.MILLILITERS, isAllergen = false, image = Image(label = "Vegetable Oil", url = "https://cdn-icons-png.freepik.com/256/1005/1005447.png?ga=GA1.1.1446863664.1727893023&semt=ais_hybrid")),
            Ingredient(name = "Baking Powder", type = IngredientType.BakingProducts, quantity = 0.0, unit = MeasurementUnit.GRAMS, isAllergen = false, image = Image(label = "Baking Powder", url = "https://cdn-icons-png.freepik.com/256/8883/8883680.png?ga=GA1.1.1446863664.1727893023&semt=ais_hybrid")),
            Ingredient(name = "Baking Soda", type = IngredientType.BakingProducts, quantity = 0.0, unit = MeasurementUnit.GRAMS, isAllergen = false, image = Image(label = "Baking Soda", url = "https://cdn-icons-png.freepik.com/256/5900/5900302.png?ga=GA1.1.1446863664.1727893023&semt=ais_hybrid")),
            Ingredient(name = "Vanilla Extract", type = IngredientType.BakingProducts, quantity = 0.0, unit = MeasurementUnit.MILLILITERS, isAllergen = false, image = Image(label = "Vanilla Extract", url = "https://cdn-icons-png.freepik.com/256/12401/12401733.png?ga=GA1.1.1446863664.1727893023&semt=ais_hybrid")),
            Ingredient(name = "Brown Sugar", type = IngredientType.BakingProducts, quantity = 0.0, unit = MeasurementUnit.GRAMS, isAllergen = false, image = Image(label = "Brown Sugar", url = "https://cdn-icons-png.freepik.com/256/17608/17608454.png?ga=GA1.1.1446863664.1727893023&semt=ais_hybrid")),

            // Herbs and Spices
            Ingredient(name = "Black Pepper", type = IngredientType.Herbs, quantity = 0.0, unit = MeasurementUnit.GRAMS, isAllergen = false, image = Image(label = "Black Pepper", url = "https://cdn-icons-png.freepik.com/256/8944/8944607.png?ga=GA1.1.1446863664.1727893023&semt=ais_hybrid")),
            Ingredient(name = "Basil", type = IngredientType.Herbs, quantity = 0.0, unit = MeasurementUnit.GRAMS, isAllergen = false, image = Image(label = "Basil", url = "https://www.freepik.com/icon/spice_4087058#fromView=search&page=1&position=12&uuid=bfa8facc-6f6b-45cc-bb40-79415e644269")),
            Ingredient(name = "Oregano", type = IngredientType.Herbs, quantity = 0.0, unit = MeasurementUnit.GRAMS, isAllergen = false, image = Image(label = "Oregano", url = "https://cdn-icons-png.freepik.com/256/5357/5357181.png?ga=GA1.1.1446863664.1727893023&semt=ais_hybrid")),
            Ingredient(name = "Thyme", type = IngredientType.Herbs, quantity = 0.0, unit = MeasurementUnit.GRAMS, isAllergen = false, image = Image(label = "Thyme", url = "https://cdn-icons-png.freepik.com/256/12386/12386229.png?ga=GA1.1.1446863664.1727893023&semt=ais_hybrid")),
            Ingredient(name = "Rosemary", type = IngredientType.Herbs, quantity = 0.0, unit = MeasurementUnit.GRAMS, isAllergen = false, image = Image(label = "Rosemary", url = "https://cdn-icons-png.freepik.com/256/12287/12287025.png?ga=GA1.1.1446863664.1727893023&semt=ais_hybrid")),
            Ingredient(name = "Cinnamon", type = IngredientType.Herbs, quantity = 0.0, unit = MeasurementUnit.GRAMS, isAllergen = false, image = Image(label = "Cinnamon", url = "https://cdn-icons-png.freepik.com/256/3944/3944330.png?ga=GA1.1.1446863664.1727893023&semt=ais_hybrid")),
            Ingredient(name = "Paprika", type = IngredientType.Herbs, quantity = 0.0, unit = MeasurementUnit.GRAMS, isAllergen = false, image = Image(label = "Paprika", url = "https://cdn-icons-png.freepik.com/256/16190/16190590.png?ga=GA1.1.1446863664.1727893023&semt=ais_hybrid")),
            Ingredient(name = "Cumin", type = IngredientType.Herbs, quantity = 0.0, unit = MeasurementUnit.GRAMS, isAllergen = false, image = Image(label = "Cumin", url = "https://cdn-icons-png.freepik.com/256/4366/4366696.png?ga=GA1.1.1446863664.1727893023&semt=ais_hybrid")),

            // Grains and Legumes
            Ingredient(name = "Rice", type = IngredientType.BakingProducts, quantity = 0.0, unit = MeasurementUnit.GRAMS, isAllergen = false, image = Image(label = "Rice", url = "https://cdn-icons-png.freepik.com/256/3983/3983406.png?ga=GA1.1.1446863664.1727893023&semt=ais_hybrid")),
            Ingredient(name = "Pasta", type = IngredientType.BakingProducts, quantity = 0.0, unit = MeasurementUnit.GRAMS, isAllergen = false, image = Image(label = "Pasta", url = "https://cdn-icons-png.freepik.com/256/3099/3099833.png?ga=GA1.1.1446863664.1727893023&semt=ais_hybrid")),
            Ingredient(name = "Quinoa", type = IngredientType.BakingProducts, quantity = 0.0, unit = MeasurementUnit.GRAMS, isAllergen = false, image = Image(label = "Quinoa", url = "https://cdn-icons-png.freepik.com/256/2353/2353921.png?ga=GA1.1.1446863664.1727893023&semt=ais_hybrid")),
            Ingredient(name = "Beans", type = IngredientType.BakingProducts, quantity = 0.0, unit = MeasurementUnit.GRAMS, isAllergen = false, image = Image(label = "Beans", url = "https://cdn-icons-png.freepik.com/256/2079/2079330.png?ga=GA1.1.1446863664.1727893023&semt=ais_hybrid")),
            Ingredient(name = "Lentils", type = IngredientType.BakingProducts, quantity = 0.0, unit = MeasurementUnit.GRAMS, isAllergen = false, image = Image(label = "Lentils", url = "https://www.freepik.com/icon/soya_8945302#fromView=search&page=1&position=7&uuid=3f4f70eb-43f7-4bb4-932d-f95a1bc270b6")),

            // Baking and Pantry
            Ingredient(name = "Cocoa Powder", type = IngredientType.BakingProducts, quantity = 0.0, unit = MeasurementUnit.GRAMS, isAllergen = false, image = Image(label = "Cocoa Powder", url = "https://cdn-icons-png.freepik.com/256/3105/3105161.png?ga=GA1.1.1446863664.1727893023&semt=ais_hybrid")),
            Ingredient(name = "Maple Syrup", type = IngredientType.BakingProducts, quantity = 0.0, unit = MeasurementUnit.MILLILITERS, isAllergen = false, image = Image(label = "Maple Syrup", url = "https://cdn-icons-png.freepik.com/256/3204/3204989.png?ga=GA1.1.1446863664.1727893023&semt=ais_hybrid")),
            Ingredient(name = "Vanilla Extract", type = IngredientType.BakingProducts, quantity = 0.0, unit = MeasurementUnit.MILLILITERS, isAllergen = false, image = Image(label = "Vanilla Extract", url = "https://cdn-icons-png.freepik.com/256/12401/12401733.png?ga=GA1.1.1446863664.1727893023&semt=ais_hybrid"))
        )

        val saveResult = repository.saveIngredients(initialIngredients)
        saveResult.onSuccess {
            println("Ingredientes iniciales guardados exitosamente")
        }.onFailure { error ->
            println("Error al guardar ingredientes iniciales: ${error.message}")
        }
    }

}
