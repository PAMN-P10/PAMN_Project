package com.pamn.letscook.presentation.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.pamn.letscook.data.repositories.RecipeInitializer
import com.pamn.letscook.data.repositories.RecipeRepository
import com.pamn.letscook.domain.models.Ingredient
import com.pamn.letscook.domain.models.Recipe
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

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

    // Título de la receta
    var title by mutableStateOf("")

    // Descripción de la receta
    var description by mutableStateOf("")

    // URI de la imagen seleccionada
    var image by mutableStateOf("")

    var imagePath: String by mutableStateOf("")

    var imageUri: Uri? by mutableStateOf(null)

    // Lista de ingredientes seleccionados (par nombre de ingrediente, cantidad y unidad)
    var selectedIngredients = mutableStateListOf<Triple<String, Double, String>>()

    private val firestore = FirebaseFirestore.getInstance()
    // Suponiendo que tienes un método para el usuario logueado y almacenamiento
    fun toggleFavorite(recipe: Recipe, userId: String) {
        val updatedRecipe = recipe.copy(isFavorite = !recipe.isFavorite)
        val updatedRecipes = _recipes.value.toMutableList()
        val index = _recipes.value.indexOfFirst { it.title == recipe.title }

        if (index != -1) {
            updatedRecipes[index] = updatedRecipe
            _recipes.value = updatedRecipes

            // Actualizar Firestore
            val favoriteDoc = firestore.collection("users")
                .document(userId)
                .collection("favorites")
                .document(recipe.title)

            if (updatedRecipe.isFavorite) {
                favoriteDoc.set(updatedRecipe.toMap()) // Guarda el favorito
            } else {
                favoriteDoc.delete() // Elimina el favorito
            }
        }
    }

    private fun saveFavorites(favoriteRecipes: List<Recipe>) {
        // Implementa la lógica para guardar favoritos
    }
    // Nueva función para cargar favoritos desde la base de datos
    fun loadFavoritesFromFirestore(userId: String) {
        firestore.collection("users")
            .document(userId)
            .collection("favorites")
            .get()
            .addOnSuccessListener { documents ->
                val favoriteTitles = documents.mapNotNull { it.id }
                _recipes.value = _recipes.value.map { recipe ->
                    recipe.copy(isFavorite = favoriteTitles.contains(recipe.title))
                }
            }
            .addOnFailureListener { exception ->
                _errorMessage.value = "Failed to load favorites: ${exception.message}"
            }
    }

    // Inicializar recetas si están vacías
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

    suspend fun loadRecipeByTitleFromRepository(title: String): Recipe? {
        return repository.getRecipeByTitle(title).getOrNull()
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

    // Métodos para manipular ingredientes seleccionados
    fun addIngredient(name: String, quantity: Double, unit: String) {
        selectedIngredients.add(Triple(name, quantity, unit))
    }

    fun removeIngredient(name: String) {
        selectedIngredients.removeIf { it.first == name }
    }

    fun updateIngredientUnit(name: String, newUnit: String) {
        val index = selectedIngredients.indexOfFirst { it.first == name }
        if (index != -1) {
            val currentIngredient = selectedIngredients[index]
            selectedIngredients[index] = Triple(currentIngredient.first, currentIngredient.second, newUnit)
        }
    }

    // Guardar imagen localmente
    fun saveImageToLocal(context: Context, imageUri: Uri) {
        val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
        val file = File(context.filesDir, "recipe_image.png")
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        }
        imagePath = file.absolutePath
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