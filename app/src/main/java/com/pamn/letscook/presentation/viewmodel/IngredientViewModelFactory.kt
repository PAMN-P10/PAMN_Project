package com.pamn.letscook.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pamn.letscook.data.repositories.IngredientRepository
import com.pamn.letscook.domain.usecases.IngredientInitializer


// La lógica de creación del ViewModel está completamente separada de la clase misma.
// Es mucho más fácil integrar ViewModelFactory con frameworks como Hilt o Dagger, donde las fábricas se generan automáticamente o se inyectan en contextos.
// Separado, sigue principios de SOLID
// Inyección de dependencias más facil de integrar


class IngredientViewModelFactory(
    private val ingredientRepository: IngredientRepository,
    private val ingredientInitializer: IngredientInitializer
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(IngredientViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return IngredientViewModel(ingredientRepository, ingredientInitializer) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}