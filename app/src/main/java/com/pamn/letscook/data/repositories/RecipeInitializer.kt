package com.pamn.letscook.data.repositories

import com.pamn.letscook.domain.models.DifficultyLevel
import com.pamn.letscook.domain.models.FilterLabels
import com.pamn.letscook.domain.models.Image
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
                mainImage = Image(label = "Classic Spaghetti Bolognese",url = "https://i.pinimg.com/736x/e3/b1/0e/e3b10e548f91c57a891f88eacc3e0eec.jpg", format = "jpg"),
                appliedFilters = listOf(
                    FilterLabels(name = "Savory"),
                    FilterLabels(name = "Gluten-free")),
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
                mainImage = Image(label = "Vegetarian Quinoa Salad", url = "https://i.pinimg.com/736x/88/fa/8d/88fa8debbcb699083d442dadfbf676f9.jpg", format = "jpg"),
                appliedFilters = listOf(
                    FilterLabels(name = "Organic"),
                    FilterLabels(name = "Gluten-free"),
                    FilterLabels(name = "Dairy-free"),),
                createdAt = LocalDateTime.now()
            ),
            Recipe(
                title = "Vegan Chocolate Avocado Mousse",
                description = "A creamy, rich dessert that's completely dairy-free and vegan-friendly.",
                author = defaultUser,
                ingredients = listOf(
                    ingredientRepository.getIngredientByName("Avocado").getOrElse {
                        throw IllegalStateException("Ingredient not found: Avocado")
                    },
                    ingredientRepository.getIngredientByName("Cocoa Powder").getOrElse {
                        throw IllegalStateException("Ingredient not found: Cocoa Powder")
                    },
                    ingredientRepository.getIngredientByName("Maple Syrup").getOrElse {
                        throw IllegalStateException("Ingredient not found: Maple Syrup")
                    },
                    ingredientRepository.getIngredientByName("Vanilla Extract").getOrElse {
                        throw IllegalStateException("Ingredient not found: Vanilla Extract")
                    },
                    ingredientRepository.getIngredientByName("Salt").getOrElse {
                        throw IllegalStateException("Ingredient not found: Salt")
                    }
                ),
                steps = listOf(
                    PreparationStep(
                        stepNumber = 1,
                        description = "Blend avocado until smooth.",
                        estimatedTime = Timer(2, TimeUnit.MINUTES),
                        requiresTimer = false,
                        imageUrl = null
                    ),
                    PreparationStep(
                        stepNumber = 2,
                        description = "Add cocoa powder, maple syrup, vanilla extract, and salt. Blend until creamy.",
                        estimatedTime = Timer(3, TimeUnit.MINUTES),
                        requiresTimer = false,
                        imageUrl = null
                    ),
                    PreparationStep(
                        stepNumber = 3,
                        description = "Chill in the refrigerator for 30 minutes before serving.",
                        estimatedTime = Timer(30, TimeUnit.MINUTES),
                        requiresTimer = true,
                        imageUrl = null
                    )
                ),
                preparationTime = 35,
                difficulty = DifficultyLevel.Beginner,
                servings = 4,
                mainImage = Image(label = "Vegan Chocolate Avocado Mousse", url = "https://i.pinimg.com/736x/f7/2b/21/f72b2162993fbca3438e51989df20f2b.jpg", format = "jpg"),
                appliedFilters = listOf(
                    FilterLabels(name = "Dairy-free"),
                    FilterLabels(name = "Sweet")),
                createdAt = LocalDateTime.now()
            ),
            Recipe(
                title = "Grilled Lemon Herb Chicken",
                description = "Juicy grilled chicken marinated in fresh herbs and zesty lemon.",
                author = defaultUser,
                ingredients = listOf(
                    ingredientRepository.getIngredientByName("Chicken Breast").getOrElse {
                        throw IllegalStateException("Ingredient not found: Chicken Breast")
                    },
                    ingredientRepository.getIngredientByName("Lemon").getOrElse {
                        throw IllegalStateException("Ingredient not found: Lemon")
                    },
                    ingredientRepository.getIngredientByName("Garlic").getOrElse {
                        throw IllegalStateException("Ingredient not found: Garlic")
                    },
                    ingredientRepository.getIngredientByName("Olive Oil").getOrElse {
                        throw IllegalStateException("Ingredient not found: Olive Oil")
                    },
                    ingredientRepository.getIngredientByName("Thyme").getOrElse {
                        throw IllegalStateException("Ingredient not found: Thyme")
                    },
                    ingredientRepository.getIngredientByName("Rosemary").getOrElse {
                        throw IllegalStateException("Ingredient not found: Rosemary")
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
                        description = "Mix lemon juice, olive oil, garlic, thyme, and rosemary to prepare the marinade.",
                        estimatedTime = Timer(5, TimeUnit.MINUTES),
                        requiresTimer = false,
                        imageUrl = null
                    ),
                    PreparationStep(
                        stepNumber = 2,
                        description = "Marinate chicken for at least 30 minutes.",
                        estimatedTime = Timer(30, TimeUnit.MINUTES),
                        requiresTimer = true,
                        imageUrl = null
                    ),
                    PreparationStep(
                        stepNumber = 3,
                        description = "Grill chicken until fully cooked, about 6 minutes per side.",
                        estimatedTime = Timer(12, TimeUnit.MINUTES),
                        requiresTimer = true,
                        imageUrl = null
                    )
                ),
                preparationTime = 50,
                difficulty = DifficultyLevel.Intermediate,
                servings = 2,
                mainImage = Image(label = "Grilled Lemon Herb Chicken", url = "https://i.pinimg.com/736x/16/14/33/161433d62e075969a72cd8ae07d40da5.jpg", format = "jpg"),
                appliedFilters = listOf(
                    FilterLabels(name = "Savory"),
                    FilterLabels(name = "Gluten-free")),
                createdAt = LocalDateTime.now()
            ),
            Recipe(
                title = "Classic Pancakes",
                description = "Fluffy, homemade pancakes perfect for a sweet breakfast.",
                author = defaultUser,
                ingredients = listOf(
                    ingredientRepository.getIngredientByName("Flour").getOrElse {
                        throw IllegalStateException("Ingredient not found: Flour")
                    },
                    ingredientRepository.getIngredientByName("Milk").getOrElse {
                        throw IllegalStateException("Ingredient not found: Milk")
                    },
                    ingredientRepository.getIngredientByName("Eggs").getOrElse {
                        throw IllegalStateException("Ingredient not found: Eggs")
                    },
                    ingredientRepository.getIngredientByName("Baking Powder").getOrElse {
                        throw IllegalStateException("Ingredient not found: Baking Powder")
                    },
                    ingredientRepository.getIngredientByName("Sugar").getOrElse {
                        throw IllegalStateException("Ingredient not found: Sugar")
                    },
                    ingredientRepository.getIngredientByName("Butter").getOrElse {
                        throw IllegalStateException("Ingredient not found: Butter")
                    },
                    ingredientRepository.getIngredientByName("Salt").getOrElse {
                        throw IllegalStateException("Ingredient not found: Salt")
                    }
                ),
                steps = listOf(
                    PreparationStep(
                        stepNumber = 1,
                        description = "Mix flour, sugar, baking powder, and salt in a bowl.",
                        estimatedTime = Timer(3, TimeUnit.MINUTES),
                        requiresTimer = false,
                        imageUrl = null
                    ),
                    PreparationStep(
                        stepNumber = 2,
                        description = "Add milk, eggs, and melted butter. Whisk until smooth.",
                        estimatedTime = Timer(3, TimeUnit.MINUTES),
                        requiresTimer = false,
                        imageUrl = null
                    ),
                    PreparationStep(
                        stepNumber = 3,
                        description = "Pour batter onto a hot griddle and cook until bubbles form. Flip and cook until golden brown.",
                        estimatedTime = Timer(10, TimeUnit.MINUTES),
                        requiresTimer = true,
                        imageUrl = null
                    )
                ),
                preparationTime = 20,
                difficulty = DifficultyLevel.Beginner,
                servings = 4,
                mainImage = Image(label = "Classic Pancakes", url = "https://i.pinimg.com/736x/ac/ce/25/acce25f7721236fd720c3638e3f448f6.jpg", format = "jpg"),
                appliedFilters = listOf(
                    FilterLabels(name = "Sweet")),
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