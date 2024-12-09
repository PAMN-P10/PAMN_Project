package com.example.pamn_project.viewmodel

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class RecipeViewModel : ViewModel() {
    // Título de la receta
    var title by mutableStateOf("")

    // Descripción de la receta
    var description by mutableStateOf("")

    // URI de la imagen seleccionada
    var selectedImageUri: Uri? by mutableStateOf(null)

    // Lista de ingredientes seleccionados (par nombre de ingrediente, cantidad y unidad)
    var selectedIngredients = mutableStateListOf<Triple<String, Double, String>>()

    // Métodos para manipular ingredientes seleccionados
    // Añadir un ingrediente (nombre, cantidad, unidad)
    fun addIngredient(name: String, quantity: Double, unit: String) {
        selectedIngredients.add(Triple(name, quantity, unit))
    }

    // Eliminar un ingrediente por su nombre
    fun removeIngredient(name: String) {
        selectedIngredients.removeIf { it.first == name }
    }

    // Actualizar la unidad de un ingrediente
    fun updateIngredientUnit(name: String, newUnit: String) {
        val index = selectedIngredients.indexOfFirst { it.first == name }
        if (index != -1) {
            // Actualizamos la unidad del ingrediente pero mantenemos el nombre y la cantidad
            val currentIngredient = selectedIngredients[index]
            selectedIngredients[index] = Triple(currentIngredient.first, currentIngredient.second, newUnit)
        }
    }

    // Reiniciar todos los datos
    fun reset() {
        title = ""
        description = ""
        selectedImageUri = null
        selectedIngredients.clear()
    }
}
