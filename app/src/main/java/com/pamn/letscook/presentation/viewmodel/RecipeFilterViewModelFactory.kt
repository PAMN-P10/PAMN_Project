package com.pamn.letscook.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class RecipeFilterViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecipeFilterViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RecipeFilterViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
