package com.pamn.letscook.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pamn.letscook.data.repositories.RecipeRepository
import com.pamn.letscook.data.repositories.RecipeInitializer

class RecipeViewModelFactory(
    private val recipeRepository: RecipeRepository,
    private val recipeInitializer: RecipeInitializer
) : ViewModelProvider.Factory {
    /*override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecipeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RecipeViewModel(recipeRepository, recipeInitializer) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }*/
}