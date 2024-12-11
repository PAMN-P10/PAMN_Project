package com.example.pamn_project.services

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream


object AuthService {
    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().reference.child("users")
    val userId: String? get() = auth.currentUser?.uid

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
     * Registra un usuario con email, contraseña, nombre de usuario y foto de perfil (Base64).
     */
    suspend fun registerUser(
        email: String,
        password: String,
        username: String,
        pfpBase64: String
    ): Result<Unit> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val user = result.user
            user?.let {
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(username)
                    .build()
                it.updateProfile(profileUpdates).await()

                // Guardamos los datos del usuario en Firebase
                Log.d("AuthService", "Saving pfpBase64: $pfpBase64")
                database.child(it.uid).setValue(
                    mapOf(
                        "email" to email,
                        "username" to username,
                        "pfp" to pfpBase64
                    )
                ).await()
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }

    }

    /**
     * Inicia sesión con email y contraseña.
     */
    suspend fun loginUser(email: String, password: String): Result<FirebaseUser?> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            Result.success(result.user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Obtiene los datos del usuario desde la base de datos
    suspend fun getUserData(uid: String): Map<String, String>? {
        val database = FirebaseDatabase.getInstance().reference
        val userSnapshot = database.child("users").child(uid).get().await()
        return if (userSnapshot.exists()) {
            userSnapshot.value as? Map<String, String>
        } else {
            null
        }
    }


    // Actualiza los datos del usuario
    suspend fun updateUserData(uid: String, data: Map<String, String>): Result<Unit> {
        return try {
            database.child(uid).updateChildren(data.toMap()).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Cierra la sesión del usuario actual.
     */
    fun logout() {
        auth.signOut()
    }

    /**
     * Obtiene el usuario autenticado actualmente.
     */
    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }
}
