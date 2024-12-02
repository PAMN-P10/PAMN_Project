package com.example.pamn_project.services

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

object FirebaseDatabaseService {

    // Obtenemos la referencia a la base de datos y apuntamos a "images/pfp"
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val pfpRef: DatabaseReference = database.reference.child("images").child("pfp")

    // Función para guardar la imagen de perfil codificada en Base64
    suspend fun saveProfileImageBase64(userId: String, base64Image: String) {
        try {
            // Referencia a la ubicación específica donde queremos guardar la imagen de perfil
            val userPfpRef = pfpRef.child(userId)

            // Guardamos la imagen codificada en Base64 bajo la clave "userId"
            userPfpRef.setValue(base64Image).await()
        } catch (e: Exception) {
            // Manejo de errores en caso de que falle la operación
            e.printStackTrace()
        }
    }

    suspend fun getProfileImageBase64(userId: String): String? {
        return try {
            val snapshot = FirebaseDatabase.getInstance()
                .getReference("images/pfp/$userId")
                .get()
                .await()

            snapshot.getValue(String::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
