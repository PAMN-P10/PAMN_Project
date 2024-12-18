package com.pamn.letscook.data.repositories

import com.pamn.letscook.domain.models.DifficultyLevel
import com.pamn.letscook.domain.models.Ingredient
import com.pamn.letscook.domain.models.PreparationStep
import com.pamn.letscook.domain.models.Recipe
import com.pamn.letscook.domain.models.Timer
import com.pamn.letscook.domain.models.User
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

class RecipeInitializer(
    private val repository: RecipeRepository,
    private val userRepository: UserRepository,
    private val ingredientRepository: IngredientRepository
) {
    suspend fun initializeRecipesIfEmpty() {
        println("Initializing recipes...")

        val result = repository.getAllRecipes()

        result.onSuccess { recipes ->
            println("Existing recipes: $recipes")
            if (recipes.isEmpty()) {
                saveInitialRecipes()
            }
        }.onFailure { error ->
            println("Error retrieving recipes: ${error.message}")
            saveInitialRecipes()
        }
    }

    private suspend fun getIngredientByName(name: String): Ingredient {
        return ingredientRepository.getIngredientByName(name).getOrElse {
            throw IllegalStateException("Ingredient not found: $name")
        }
    }

    private suspend fun saveInitialRecipes() {
        // Create default user
        val defaultUser = User(
            userId = "default_chef",
            username = "Master Chef",
            email = "chef@letscook.com",
            password = "securePassword123"
        )

        // Save the default user first
        userRepository.saveUser(defaultUser)

        // Fetch ingredients from the database
        val initialRecipes = listOf(
            Recipe(
                title = "Classic Spaghetti Bolognese",
                description = "A hearty Italian pasta dish with rich meat sauce",
                author = defaultUser,
                ingredients = listOf(
                    ingredientRepository.getIngredientByName("Tomato").getOrElse {
                        throw IllegalStateException("Ingredient not found: Tomato")
                    },
                    ingredientRepository.getIngredientByName("Onion").getOrElse {
                        throw IllegalStateException("Ingredient not found: Onion")
                    },
                    ingredientRepository.getIngredientByName("Garlic").getOrElse {
                        throw IllegalStateException("Ingredient not found: Garlic")
                    },
                    ingredientRepository.getIngredientByName("Ground Beef").getOrElse {
                        throw IllegalStateException("Ingredient not found: Ground Beef")
                    },
                    ingredientRepository.getIngredientByName("Olive Oil").getOrElse {
                        throw IllegalStateException("Ingredient not found: Olive Oil")
                    },
                    ingredientRepository.getIngredientByName("Salt").getOrElse {
                        throw IllegalStateException("Ingredient not found: Salt")
                    },
                    ingredientRepository.getIngredientByName("Black Pepper").getOrElse {
                        throw IllegalStateException("Ingredient not found: Black Pepper")
                    },
                    ingredientRepository.getIngredientByName("Pasta").getOrElse {
                        throw IllegalStateException("Ingredient not found: Pasta")
                    }
                ),
                steps = listOf(
                    PreparationStep(
                        stepNumber = 1,
                        description = "Heat olive oil in a large pan",
                        estimatedTime = Timer(3, TimeUnit.MINUTES),
                        requiresTimer = true,
                        imageUrl = null
                    ),
                    PreparationStep(
                        stepNumber = 2,
                        description = "Sauté chopped onions and minced garlic until translucent",
                        estimatedTime = Timer(5, TimeUnit.MINUTES),
                        requiresTimer = true,
                        imageUrl = null
                    ),
                    PreparationStep(
                        stepNumber = 3,
                        description = "Add ground beef and cook until browned, breaking it into small pieces",
                        estimatedTime = Timer(10, TimeUnit.MINUTES),
                        requiresTimer = true,
                        imageUrl = null
                    ),
                    PreparationStep(
                        stepNumber = 4,
                        description = "Add chopped tomatoes and season with salt and black pepper",
                        estimatedTime = Timer(5, TimeUnit.MINUTES),
                        requiresTimer = false,
                        imageUrl = null
                    ),
                    PreparationStep(
                        stepNumber = 5,
                        description = "Simmer the sauce on low heat until it thickens",
                        estimatedTime = Timer(15, TimeUnit.MINUTES),
                        requiresTimer = true,
                        imageUrl = null
                    ),
                    PreparationStep(
                        stepNumber = 6,
                        description = "Cook pasta in boiling salted water according to package instructions",
                        estimatedTime = Timer(10, TimeUnit.MINUTES),
                        requiresTimer = true,
                        imageUrl = null
                    ),
                    PreparationStep(
                        stepNumber = 7,
                        description = "Drain pasta and mix with the sauce. Serve hot.",
                        estimatedTime = Timer(2, TimeUnit.MINUTES),
                        requiresTimer = false,
                        imageUrl = null
                    )
                ),
                preparationTime = 50,
                difficulty = DifficultyLevel.Intermediate,
                servings = 4,
                mainImage = null,
                appliedFilters = emptyList(),
                createdAt = LocalDateTime.now()
            ),
            Recipe(
                title = "Vegetarian Quinoa Salad",
                description = "A nutritious and refreshing salad packed with protein",
                author = defaultUser,
                ingredients = listOf(
                    ingredientRepository.getIngredientByName("Quinoa").getOrElse {
                        throw IllegalStateException("Ingredient not found: Quinoa")
                    },
                    ingredientRepository.getIngredientByName("Tomato").getOrElse {
                        throw IllegalStateException("Ingredient not found: Tomato")
                    },
                    ingredientRepository.getIngredientByName("Cucumber").getOrElse {
                        throw IllegalStateException("Ingredient not found: Cucumber")
                    },
                    ingredientRepository.getIngredientByName("Onion").getOrElse {
                        throw IllegalStateException("Ingredient not found: Onion")
                    },
                    ingredientRepository.getIngredientByName("Olive Oil").getOrElse {
                        throw IllegalStateException("Ingredient not found: Olive Oil")
                    },
                    ingredientRepository.getIngredientByName("Lemon").getOrElse {
                        throw IllegalStateException("Ingredient not found: Lemon")
                    },
                    ingredientRepository.getIngredientByName("Salt").getOrElse {
                        throw IllegalStateException("Ingredient not found: Salt")
                    },
                    ingredientRepository.getIngredientByName("Black Pepper").getOrElse {
                        throw IllegalStateException("Ingredient not found: Black Pepper")
                    }
                ),
                steps = listOf(
                    PreparationStep(
                        stepNumber = 1,
                        description = "Rinse quinoa thoroughly",
                        estimatedTime = Timer(2, TimeUnit.MINUTES),
                        requiresTimer = false,
                        imageUrl = null
                    ),
                    PreparationStep(
                        stepNumber = 2,
                        description = "Cook quinoa in water according to package instructions",
                        estimatedTime = Timer(15, TimeUnit.MINUTES),
                        requiresTimer = true,
                        imageUrl = null
                    ),
                    PreparationStep(
                        stepNumber = 3,
                        description = "Chop tomatoes, cucumber, and onion into small pieces",
                        estimatedTime = Timer(5, TimeUnit.MINUTES),
                        requiresTimer = false,
                        imageUrl = null
                    ),
                    PreparationStep(
                        stepNumber = 4,
                        description = "Mix cooked quinoa with chopped vegetables",
                        estimatedTime = Timer(2, TimeUnit.MINUTES),
                        requiresTimer = false,
                        imageUrl = null
                    ),
                    PreparationStep(
                        stepNumber = 5,
                        description = "Dress with olive oil, lemon juice, salt, and black pepper",
                        estimatedTime = Timer(3, TimeUnit.MINUTES),
                        requiresTimer = false,
                        imageUrl = null
                    ),
                    PreparationStep(
                        stepNumber = 6,
                        description = "Chill for 10 minutes before serving",
                        estimatedTime = Timer(10, TimeUnit.MINUTES),
                        requiresTimer = true,
                        imageUrl = null
                    )
                ),
                preparationTime = 35,
                difficulty = DifficultyLevel.Beginner,
                servings = 2,
                mainImage = null,
                appliedFilters = emptyList(),
                createdAt = LocalDateTime.now()
            )
        )

        val saveResult = repository.saveRecipes(initialRecipes)
        saveResult.onSuccess {
            println("Initial recipes saved successfully")
        }.onFailure { error ->
            println("Error saving initial recipes: ${error.message}")
        }
    }
}