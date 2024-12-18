package com.pamn.letscook.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pamn.letscook.data.repositories.RecipeInitializer
import com.pamn.letscook.data.repositories.RecipeRepository
import com.pamn.letscook.domain.models.Ingredient
import com.pamn.letscook.domain.models.Recipe
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RecipeViewModel(
    private val repository: RecipeRepository,
    private val recipeInitializer: RecipeInitializer
) : ViewModel() {

    // Estados para la UI
    private val _recipes = MutableStateFlow<List<Recipe>>(emptyList())
    val recipes: StateFlow<List<Recipe>> get() = _recipes

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> get() = _errorMessage

    fun initializeRecipes() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                recipeInitializer.initializeRecipesIfEmpty()
                loadRecipes()
            } catch (e: Exception) {
                _errorMessage.value = "Error initializing recipes: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Cargar todas las recetas
    fun loadRecipes() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            repository.getAllRecipes()
                .onSuccess { fetchedRecipes ->
                    _recipes.value = fetchedRecipes
                }
                .onFailure { error ->
                    handleRecipeError(error)
                }
            _isLoading.value = false
        }
    }

    // Guardar una receta
    fun saveRecipe(recipe: Recipe) {
        viewModelScope.launch {
            _isLoading.value = true
            repository.saveRecipe(recipe)
                .onSuccess {
                    loadRecipes() // Recargar las recetas después de guardar
                }
                .onFailure { error ->
                    handleRecipeError(error)
                }
            _isLoading.value = false
        }
    }

    // Cargar receta por título
    fun loadRecipeByTitle(title: String) {
        viewModelScope.launch {
            _isLoading.value = true
            repository.getRecipeByTitle(title)
                .onSuccess { recipe ->
                    _recipes.value = listOf(recipe)
                }
                .onFailure { error ->
                    handleRecipeError(error)
                }
            _isLoading.value = false
        }
    }

    // Filtrar recetas por nombre
    fun filterRecipesByName(query: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            repository.getAllRecipes()
                .onSuccess { allRecipes ->
                    _recipes.value = allRecipes.filter { recipe ->
                        recipe.title.contains(query, ignoreCase = true)
                    }
                }
                .onFailure { error ->
                    handleRecipeError(error)
                }

            _isLoading.value = false
        }
    }

    // Filtrar recetas por fecha de creación
    fun filterRecipesByDate(ascending: Boolean) {
        viewModelScope.launch {
            _isLoading.value = true
            repository.getAllRecipes()
                .onSuccess { allRecipes ->
                    _recipes.value = if (ascending) {
                        allRecipes.sortedBy { it.createdAt }
                    } else {
                        allRecipes.sortedByDescending { it.createdAt }
                    }
                }
                .onFailure { error ->
                    handleRecipeError(error)
                }
            _isLoading.value = false
        }
    }

    fun filterRecipesByIngredients(ingredients: List<Ingredient>) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val allRecipes = repository.getAllRecipes().getOrThrow()
                val filteredRecipes = if (ingredients.isEmpty()) {
                    allRecipes
                } else {
                    allRecipes.filter { recipe ->
                        ingredients.all { ingredient ->
                            recipe.ingredients.any { it.name == ingredient.name }
                        }
                    }
                }
                _recipes.value = filteredRecipes
            } catch (error: Throwable) {
                handleRecipeError(error)
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Manejo centralizado de errores
    private fun handleRecipeError(error: Throwable) {
        when (error) {
            is RecipeRepository.RecipeError.NetworkError -> {
                _errorMessage.value = "Network error: Check your internet connection."
            }
            is RecipeRepository.RecipeError.DatabaseError -> {
                _errorMessage.value = "Database error: Unable to fetch or save data."
            }
            is RecipeRepository.RecipeError.NotFoundError -> {
                _errorMessage.value = "Recipe not found."
            }
            else -> {
                _errorMessage.value = "Unknown error: ${error.message}"
            }
        }
    }
}
