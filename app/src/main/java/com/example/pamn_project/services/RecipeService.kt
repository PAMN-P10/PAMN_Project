package com.example.pamn_project.services

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.example.pamn_project.models.Recipe

class RecipeService {

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val recipesRef: DatabaseReference = database.getReference("recipes")

    // Función para agregar una receta a Firebase Database
    fun addRecipe(recipe: Recipe, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        val recipeId = recipesRef.push().key // Genera un id único para la receta
        if (recipeId != null) {
            recipesRef.child(recipeId).setValue(recipe)
                .addOnSuccessListener {
                    onSuccess()
                }
                .addOnFailureListener { exception ->
                    onFailure(exception.message ?: "Error al subir receta.")
                }
        } else {
            onFailure("Error al generar id de receta.")
        }
    }
}
