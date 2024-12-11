package com.example.pamn_project.models

data class Recipe(
    val title: String = "",
    val description: String = "",
    val imageUri: String = "",  // Cambiado a imageUri
    val ingredients: List<Ingredient> = listOf(),
    val steps: List<Step> = listOf()
)

data class Ingredient(
    val name: String = "",
    val quantity: Double = 0.0,
    val unit: String = ""
)

data class Step(
    val stepNumber: Int = 0,
    val instruction: String = "",
    val timer: String = ""  // Puede ser una cadena vacía si no hay temporizador
)
