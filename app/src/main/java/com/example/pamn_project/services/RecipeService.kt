package com.example.pamn_project.services

import com.google.firebase.database.FirebaseDatabase

object RecipeService {
    private val database = FirebaseDatabase.getInstance().getReference("recipes")

    fun uploadRecipe(recipe: Map<String, Any>) {
        val newRecipeRef = database.push() // Generar una nueva referencia única
        newRecipeRef.setValue(recipe).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                println("Recipe uploaded successfully!")
            } else {
                println("Failed to upload recipe: ${task.exception?.message}")
            }
        }
    }
}







