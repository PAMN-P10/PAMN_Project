package com.example.pamn_project.viewmodels

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.example.pamn_project.services.RecipeService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class RecipeViewModel : ViewModel() {
    // Título de la receta
    var title by mutableStateOf("")
        private set

    // Descripción de la receta
    var description by mutableStateOf("")
        private set

    // Imagen de la receta (en Base64)
    var imageBase64 by mutableStateOf("")
        private set

    // Lista de ingredientes añadidos
    var ingredients = mutableStateListOf<RecipeService.Ingredient>()
        private set

    // Pasos de la receta
    var instructions = mutableStateListOf<String>()
        private set

    // Tiempo de cocción
    var cookingTime by mutableStateOf("00:00:00")
        private set

    // Estado para manejar errores o mensajes
    private val _uiState = MutableStateFlow("")
    val uiState: StateFlow<String> = _uiState

    // Actualizar título
    fun updateTitle(newTitle: String) {
        title = newTitle
    }

    // Actualizar descripción
    fun updateDescription(newDescription: String) {
        description = newDescription
    }

    // Actualizar imagen (Base64)
    fun updateImage(base64Image: String) {
        imageBase64 = base64Image
    }

    // Agregar un ingrediente
    fun addIngredient(ingredient: RecipeService.Ingredient) {
        ingredients.add(ingredient)
    }

    // Eliminar un ingrediente por su índice
    fun removeIngredient(index: Int) {
        if (index in ingredients.indices) {
            ingredients.removeAt(index)
        }
    }

    // Actualizar un ingrediente existente
    fun updateIngredient(index: Int, updatedIngredient: RecipeService.Ingredient) {
        if (index in ingredients.indices) {
            ingredients[index] = updatedIngredient
        }
    }

    // Agregar un paso a las instrucciones
    fun addInstruction(step: String) {
        instructions.add(step)
    }

    // Eliminar un paso de las instrucciones por su índice
    fun removeInstruction(index: Int) {
        if (index in instructions.indices) {
            instructions.removeAt(index)
        }
    }

    // Actualizar tiempo de cocción
    fun updateCookingTime(time: String) {
        cookingTime = time
    }

    // Limpiar todos los datos (opcional para reiniciar el formulario)
    fun clearAll() {
        title = ""
        description = ""
        imageBase64 = ""
        ingredients.clear()
        instructions.clear()
        cookingTime = "00:00:00"
    }

    // Manejar mensajes de estado o errores
    fun updateUiState(message: String) {
        _uiState.update { message }
    }
}
