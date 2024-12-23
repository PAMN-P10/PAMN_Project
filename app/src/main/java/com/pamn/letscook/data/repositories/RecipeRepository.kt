package com.pamn.letscook.data.repositories

import com.google.firebase.firestore.FirebaseFirestore
import com.pamn.letscook.data.repositories.IngredientRepository
import com.pamn.letscook.domain.models.DifficultyLevel
import com.pamn.letscook.domain.models.Image
import com.pamn.letscook.domain.models.Ingredient
import com.pamn.letscook.domain.models.IngredientType
import com.pamn.letscook.domain.models.PreparationStep
import com.pamn.letscook.domain.models.Recipe
import com.pamn.letscook.domain.models.Timer
import com.pamn.letscook.domain.models.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.tasks.await
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

class RecipeRepository(
    private val firestore: FirebaseFirestore,
    private val userRepository: UserRepository,
    private val ingredientRepository: IngredientRepository
) {
    /*
    // Error handling similar to IngredientRepository
    sealed class RecipeError : Exception() {
        object NetworkError : RecipeError() {
            private fun readResolve(): Any = NetworkError
        }

        object DatabaseError : RecipeError() {
            private fun readResolve(): Any = DatabaseError
        }

        data class NotFoundError(override val message: String) : RecipeError()
        data class ParseError(override val message: String) : RecipeError()
    }

    // Save a single recipe
    suspend fun saveRecipe(recipe: Recipe): Result<Unit> {
        return runCatching {
            withContext(Dispatchers.IO) {
                // Validate recipe before saving
                require(recipe.title.isNotBlank()) { "Recipe title cannot be empty" }
                require(recipe.ingredients.isNotEmpty()) { "Recipe must have at least one ingredient" }
                require(recipe.steps.isNotEmpty()) { "Recipe must have at least one preparation step" }

                val recipeMap = mapOf(
                    "title" to recipe.title,
                    "description" to recipe.description,
                    "author" to recipe.author.userId,
                    "ingredients" to recipe.ingredients.map {ingredient ->
                        mapOf(
                            "name" to ingredient.name,
                            "type" to ingredient.type.name,
                            "quantity" to ingredient.quantity,
                            "isAllergen" to ingredient.isAllergen,
                            "image" to mapOf(
                                "label" to ingredient.image.label,
                                "url" to ingredient.image.url,
                                "format" to ingredient.image.format,
                                "width" to ingredient.image.width,
                                "height" to ingredient.image.height
                            )
                        )
                    },
                    "steps" to recipe.steps.map {
                        mapOf(
                            "stepNumber" to it.stepNumber,
                            "description" to it.description,
                            "estimatedTime" to it.estimatedTime,
                            "requiresTimer" to it.requiresTimer,
                            "imageUrl" to it.imageUrl
                        )
                    },
                    "preparationTime" to recipe.preparationTime,
                    "difficulty" to recipe.difficulty.name,
                    "mainImage" to (recipe.mainImage?.let {
                        mapOf(
                            "label" to it.label,
                            "url" to it.url,
                            "format" to it.format,
                            "width" to it.width,
                            "height" to it.height
                        )
                    } ?: emptyMap()),
                    "servings" to recipe.servings,
                    "appliedFilters" to recipe.appliedFilters.map { it.name },
                    "createdAt" to recipe.createdAt.toString()
                )

                try {
                    firestore.collection("recipes")
                        .document(recipe.title) // Using title as document ID, might want to use a unique identifier
                        .set(recipeMap)
                        .await()
                    Unit
                } catch (e: Exception) {
                    throw RecipeError.DatabaseError
                }
            }
        }.onFailure { error ->
            println("Error saving recipe: ${error.message}")
        }
    }

    suspend fun saveRecipes(recipes: List<Recipe>): Result<Void> {
        return withContext(Dispatchers.IO) {
            runCatching {
                val batch = firestore.batch()

                recipes.forEach { recipe ->
                    //saveRecipe(recipe)
                    val result = saveRecipe(recipe)
                    if (result.isFailure) {
                        throw result.exceptionOrNull() ?: RecipeError.DatabaseError
                    }

                    //batch.set(recipeRef, recipeMap)
                }

                // Ejecutar el batch para guardar todas las recetas
                batch.commit().await()
            }.onFailure { error ->
                println("Error saving recipes: ${error.message}")
            }
        }
    }

    /**
     * suspend fun saveRecipes(recipes: List<Recipe>): Result<Unit> {
     *     return withContext(Dispatchers.IO) {
     *         runCatching {
     *             val batch = firestore.batch()
     *
     *             recipes.forEach { recipe ->
     *                 val recipeRef = firestore.collection("recipes").document(recipe.id ?: recipe.title)
     *                 val recipeMap = mapOf(
     *                     "title" to recipe.title,
     *                     "description" to recipe.description,
     *                     "author" to recipe.author.userId,
     *                     "ingredients" to recipe.ingredients.map { it.name },
     *                     "steps" to recipe.steps.map {
     *                         mapOf(
     *                             "stepNumber" to it.stepNumber,
     *                             "description" to it.description,
     *                             "estimatedTime" to mapOf(
     *                                 "hours" to it.estimatedTime.hours,
     *                                 "minutes" to it.estimatedTime.minutes
     *                             ),
     *                             "requiresTimer" to it.requiresTimer,
     *                             "imageUrl" to it.imageUrl
     *                         )
     *                     },
     *                     "preparationTime" to recipe.preparationTime,
     *                     "difficulty" to recipe.difficulty.name,
     *                     "mainImage" to (recipe.mainImage?.let {
     *                         mapOf(
     *                             "label" to it.label,
     *                             "url" to it.url,
     *                             "format" to it.format,
     *                             "width" to it.width,
     *                             "height" to it.height
     *                         )
     *                     } ?: emptyMap()),
     *                     "servings" to recipe.servings,
     *                     "appliedFilters" to recipe.appliedFilters.map { it.name },
     *                     "createdAt" to recipe.createdAt.toString()
     *                 )
     *
     *                 batch.set(recipeRef, recipeMap)
     *             }
     *
     *             // Ejecutar el batch para guardar todas las recetas
     *             batch.commit().await()
     *         }.onFailure { error ->
     *             println("Error saving recipes: ${error.message}")
     *         }
     *     }
     * }
     *
     */


    // Get all recipes
    suspend fun getAllRecipes(): Result<List<Recipe>> {
        return runCatching {
            withContext(Dispatchers.IO) {
                val snapshot = try {
                    firestore.collection("recipes")
                        .get()
                        .await()
                } catch (e: Exception) {
                    throw RecipeError.NetworkError
                }

                val recipes = snapshot.documents.mapNotNull { document ->
                    try {
                        // Fetch the full author details
                        val authorId = document.getString("author")
                        val authorResult = authorId?.let { userRepository.getUserById(it) }

                        authorResult?.getOrNull()?.let { author ->
                            Recipe(
                                title = document.getString("title") ?: return@mapNotNull null,
                                description = document.getString("description") ?: "",
                                author = author,
                                ingredients = (document.get("ingredients") as? List<Map<String, Any>>)?.map { ingredientMap ->
                                    Ingredient(
                                        name = ingredientMap["name"] as? String ?: "",
                                        type = IngredientType.valueOf(ingredientMap["type"] as? String ?: "OTHER"),
                                        quantity = (ingredientMap["quantity"] as? Number)?.toDouble() ?: 0.0,
                                        isAllergen = ingredientMap["isAllergen"] as? Boolean ?: false,
                                        image = let {
                                            val imageMap = ingredientMap["image"] as? Map<String, Any>
                                            Image(
                                                label = imageMap?.get("label") as? String ?: "",
                                                url = imageMap?.get("url") as? String ?: "",
                                                format = imageMap?.get("format") as? String,
                                                width = (imageMap?.get("width") as? Number)?.toInt() ?: 0,
                                                height = (imageMap?.get("height") as? Number)?.toInt() ?: 0
                                            )
                                        }
                                    )
                                } ?: emptyList(),
                                steps = (document.get("steps") as? List<Map<String, Any>>)?.map { stepMap ->
                                    PreparationStep(
                                        stepNumber = (stepMap["stepNumber"] as? Number)?.toInt() ?: 0,
                                        description = stepMap["description"] as? String ?: "",
                                        estimatedTime = stepMap["estimatedTime"]?.let {
                                            val durationInSeconds = (it as? Map<String, Any>)?.get("seconds") as? Int ?: 0
                                            // Si tiene una unidad de tiempo en el mapa, se usa esa. De lo contrario, se usa SECONDS por defecto.
                                            val timeUnit = TimeUnit.SECONDS // EA cambiar si quiero poner otra unidad de tiempo
                                            Timer(durationInSeconds, timeUnit)
                                        } ?: Timer(0, TimeUnit.SECONDS),  // Valor por defecto si no existe estimatedTime
                                        requiresTimer = stepMap["requiresTimer"] as? Boolean ?: false,
                                        imageUrl = stepMap["imageUrl"] as? String
                                    )
                                } ?: emptyList(),
                                preparationTime = (document.get("preparationTime") as? Number)?.toInt() ?: 0,
                                difficulty = DifficultyLevel.valueOf(document.getString("difficulty") ?: "MEDIUM"),
                                mainImage = null, // You'd need to parse the image map
                                servings = (document.get("servings") as? Number)?.toInt() ?: 4,
                                appliedFilters = emptyList(), // You'd need to parse filters
                                createdAt = LocalDateTime.parse(document.getString("createdAt"))
                            )
                        }
                    } catch (e: Exception) {
                        println("Error processing recipe document: ${document.id}")
                        null
                    }
                }

                if (recipes.isEmpty()) {
                    throw RecipeError.NotFoundError("No recipes found")
                }

                recipes
            }
        }.onFailure { error ->
            when (error) {
                is RecipeError.NetworkError -> println("Network error retrieving recipes")
                is RecipeError.NotFoundError -> println("No recipes found")
                is RecipeError.ParseError -> println("Error parsing recipe data")
                else -> println("Unknown error: ${error.message}")
            }
        }
    }

    // Get recipe by title (or unique identifier)
    suspend fun getRecipeByTitle(title: String): Result<Recipe> {
        return runCatching {
            withContext(Dispatchers.IO) {
                val documentSnapshot = try {
                    firestore.collection("recipes")
                        .document(title)
                        .get()
                        .await()
                } catch (e: Exception) {
                    throw RecipeError.NetworkError
                }

                if (!documentSnapshot.exists()) {
                    throw RecipeError.NotFoundError("Recipe not found: $title")
                }

                try {
                    // Similar parsing logic as in getAllRecipes
                    val authorId = documentSnapshot.getString("author")
                    val authorResult = authorId?.let { userRepository.getUserById(it) }

                    authorResult?.getOrNull()?.let { author ->
                        Recipe(
                            title = documentSnapshot.getString("title") ?:
                            throw RecipeError.ParseError("Invalid title"),
                            description = documentSnapshot.getString("description") ?: "",
                            author = author,
                            ingredients = (documentSnapshot.get("ingredients") as? List<Map<String, Any>>)?.map { ingredientMap ->
                                Ingredient(
                                    name = ingredientMap["name"] as? String ?: "",
                                    type = IngredientType.valueOf(ingredientMap["type"] as? String ?: "OTHER"),
                                    quantity = (ingredientMap["quantity"] as? Number)?.toDouble() ?: 0.0,
                                    isAllergen = ingredientMap["isAllergen"] as? Boolean ?: false,
                                    image = let {
                                        val imageMap = ingredientMap["image"] as? Map<String, Any>
                                        Image(
                                            label = imageMap?.get("label") as? String ?: "",
                                            url = imageMap?.get("url") as? String ?: "",
                                            format = imageMap?.get("format") as? String,
                                            width = (imageMap?.get("width") as? Number)?.toInt() ?: 0,
                                            height = (imageMap?.get("height") as? Number)?.toInt() ?: 0
                                        )
                                    }
                                )
                            } ?: emptyList(),
                            steps = emptyList(), // Similar to ingredients, would need separate fetching
                            preparationTime = (documentSnapshot.get("preparationTime") as? Number)?.toInt() ?: 0,
                            difficulty = DifficultyLevel.valueOf(
                                documentSnapshot.getString("difficulty") ?: "MEDIUM"
                            ),
                            mainImage = null, // Would need to parse image map
                            servings = (documentSnapshot.get("servings") as? Number)?.toInt() ?: 4,
                            appliedFilters = emptyList(), // Would need to parse filters
                            createdAt = LocalDateTime.parse(documentSnapshot.getString("createdAt"))
                        )
                    } ?: throw RecipeError.ParseError("Could not retrieve author")
                } catch (e: Exception) {
                    throw RecipeError.ParseError("Error parsing recipe")
                }
            }
        }.onFailure { error ->
            when (error) {
                is RecipeError.NetworkError -> println("Network error retrieving recipe")
                is RecipeError.NotFoundError -> println("Recipe not found")
                is RecipeError.ParseError -> println("Error parsing recipe data")
                else -> println("Unknown error: ${error.message}")
            }
        }
    }*/
}