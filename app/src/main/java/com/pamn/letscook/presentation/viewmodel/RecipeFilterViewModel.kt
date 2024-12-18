package com.pamn.letscook.presentation.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pamn.letscook.domain.models.FilterLabels
import com.pamn.letscook.domain.models.Recipe
import com.pamn.letscook.domain.useCases.FilterRecipesUseCase

class RecipeFilterViewModel(
    private val filterRecipesUseCase: FilterRecipesUseCase // Inyección del caso de uso
) : ViewModel() {

    private val _filteredRecipes = MutableLiveData<List<Recipe>>()
    val filteredRecipes: LiveData<List<Recipe>> = _filteredRecipes

    // Estado actual de los filtros
    private val _filters = MutableLiveData<List<FilterLabels>>()
    val filters: LiveData<List<FilterLabels>> = _filters

    // Actualiza los filtros y filtra las recetas
    fun updateFilters(filters: List<FilterLabels>, allRecipes: List<Recipe>) {
        _filters.value = filters
        _filteredRecipes.value = filterRecipesUseCase(allRecipes, filters)
    }
}
