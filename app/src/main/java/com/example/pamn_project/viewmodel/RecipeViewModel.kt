package com.example.pamn_project.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Base64
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class RecipeViewModel : ViewModel() {
    // Título de la receta
    var title by mutableStateOf("")

    // Descripción de la receta
    var description by mutableStateOf("")

    // URI de la imagen seleccionada
    var image by mutableStateOf("")

    var imagePath: String by mutableStateOf("")

    var imageUri: Uri? by mutableStateOf(null)

    fun saveImageUri(uri: Uri) {
        imageUri = uri
    }

    // Lista de ingredientes seleccionados (par nombre de ingrediente, cantidad y unidad)
    var selectedIngredients = mutableStateListOf<Triple<String, Double, String>>()

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

    // Nueva función para guardar la imagen localmente, si fuera necesario
    fun saveImageToLocal(context: Context, imageUri: Uri) {
        val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
        val file = File(context.filesDir, "recipe_image.png") // El archivo se guarda en el almacenamiento interno
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        }
        imagePath = file.absolutePath // Guardamos solo la ruta
    }
}