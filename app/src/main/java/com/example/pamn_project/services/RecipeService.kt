package com.example.pamn_project.services

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.util.UUID

object RecipeService {
    private val database = FirebaseDatabase.getInstance().reference.child("recipes")

    /**
     * Convierte una imagen a Base64.
     */
    fun convertImageToBase64(context: Context, imageUri: Uri): String {
        val inputStream = context.contentResolver.openInputStream(imageUri)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    /**
     * Decodifica una imagen Base64 a Bitmap.
     */
    fun decodeBase64ToBitmap(base64String: String): Bitmap? {
        return try {
            val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Sube una receta al Firebase Realtime Database.
     */
    suspend fun uploadRecipe(
        title: String,
        description: String,
        imageBase64: String,
        ingredients: List<Map<String, String>>,
        steps: List<Map<String, Any>>
    ): Result<Unit> {
        return try {
            val currentUser = FirebaseAuth.getInstance().currentUser
                ?: return Result.failure(Exception("User not authenticated"))

            // Crear un ID único para la receta
            val recipeId = UUID.randomUUID().toString()

            // Preparar los datos de la receta
            val recipeData = mapOf(
                "title" to title,
                "description" to description,
                "image" to imageBase64,
                "ingredients" to ingredients,
                "steps" to steps,
                "userId" to currentUser.uid // Asociar receta al usuario actual
            )

            // Subir los datos al nodo de Firebase
            database.child(recipeId).setValue(recipeData).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("RecipeService", "Failed to upload recipe", e)
            Result.failure(e)
        }
    }
}