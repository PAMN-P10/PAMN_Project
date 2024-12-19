package com.pamn.letscook.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.pamn.letscook.domain.models.FilterLabels
import com.pamn.letscook.domain.models.Recipe
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class RecipeFilterViewModel : ViewModel() {

    // Estado de los filtros disponibles
    private val _filters = MutableStateFlow(
        listOf(
            FilterLabels(name = "Organic"),
            FilterLabels(name = "Gluten-free"),
            FilterLabels(name = "Dairy-free"),
            FilterLabels(name = "Sweet"),
            FilterLabels(name = "Savory")
        )
    )
    val filters: StateFlow<List<FilterLabels>> = _filters

    // Recetas filtradas
    private val _filteredRecipes = MutableStateFlow<List<Recipe>>(emptyList())
    val filteredRecipes: StateFlow<List<Recipe>> = _filteredRecipes

    /**
     * Actualiza las recetas filtradas en función de los filtros seleccionados.
     */
    fun updateFilters(filters: List<FilterLabels>, allRecipes: List<Recipe>) {
        // Actualiza los filtros activos
        _filters.value = filters

        // Filtra las recetas basándose en los filtros seleccionados
        val activeFilters = filters.filter { it.enabled.value }
        _filteredRecipes.value = if (activeFilters.isEmpty()) {
            allRecipes // Si no hay filtros activos, muestra todas las recetas
        } else {
            allRecipes.filter { recipe ->
                activeFilters.all { filter ->
                    recipe.appliedFilters.any { it.name.contains(filter.name, ignoreCase = true) }
                }
            }
        }
    }

    /**
     * Cambia el estado de selección de un filtro.
     */
    fun toggleFilterSelection(filter: FilterLabels) {
        _filters.value = _filters.value.map {
            if (it.name == filter.name) it.apply { enabled.value = !enabled.value } else it
        }
    }
}
