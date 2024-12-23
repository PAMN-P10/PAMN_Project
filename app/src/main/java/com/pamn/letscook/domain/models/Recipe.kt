package com.pamn.letscook.domain.models

import java.time.LocalDateTime

data class Recipe(
    val title: String,
    val description: String,
    val author: User,
    val ingredients: List<Ingredient>,
    val steps: List<PreparationStep>,
    val preparationTime: Int,
    val difficulty: DifficultyLevel,
    val mainImage: Image? = null, // no es image de android media sino el objeto Image creado
    val servings: Int = 4,
    val appliedFilters: List<FilterLabels> = emptyList(), // filtros para la busqueda
    val createdAt: LocalDateTime = LocalDateTime.now(), //fecha de creación
    val isFavorite: Boolean = false

) {
    fun adjustServings(newServings: Int): Recipe {
        // Recalculate ingredient quantities based on new servings
        val adjustedIngredients = ingredients.map { ingredient ->
            ingredient.copy(
                quantity = ingredient.quantity * (newServings.toDouble() / servings)
            )
        }

        return copy(
            ingredients = adjustedIngredients,
            servings = newServings
        )
    }

    fun applyFilters(filters: List<FilterLabels>): Recipe {
        return copy(appliedFilters = filters)
    }

    fun toMap(): Map<String, Any?> {
        return mapOf(
            "title" to title,
            "description" to description,
            "author" to mapOf(
                "name" to author.username,
                "email" to author.email, // Ajusta según las propiedades de User
                "profileImage" to author.profileImage
            ),
            "ingredients" to ingredients.map { ingredient ->
                mapOf(
                    "name" to ingredient.name,
                    "quantity" to ingredient.quantity,
                    "unit" to ingredient.unit
                )
            },
            "steps" to steps.map { step ->
                mapOf(
                    "order" to step.stepNumber,
                    "description" to step.description
                )
            },
            "preparationTime" to preparationTime,
            "difficulty" to difficulty.name,
            "mainImage" to mainImage?.url, // Si Image tiene una URL o similar
            "servings" to servings,
            "appliedFilters" to appliedFilters.map { it.name },
            "createdAt" to createdAt.toString(),
            "isFavorite" to isFavorite
        )
    }

}