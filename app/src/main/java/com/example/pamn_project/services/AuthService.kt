package com.example.pamn_project.services

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

object AuthService {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().reference

    suspend fun registerUser(email: String, password: String, username: String): Result<FirebaseUser?> {
        return try {
            // Crear usuario con email y password
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val user = result.user

            // Actualizar perfil del usuario con el username
            user?.let {
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(username)
                    .build()
                it.updateProfile(profileUpdates).await()
            }

            // Guardar el username en la base de datos (opcional, para búsquedas rápidas)
            user?.uid?.let { uid ->
                val userData = mapOf("username" to username, "email" to email)
                database.child("users").child(uid).setValue(userData).await()
            }

            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun loginUser(email: String, password: String): Result<FirebaseUser?> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            Result.success(result.user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun logout() {
        auth.signOut()
    }

    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }
}
