package com.pamn.letscook.domain.useCases

import com.pamn.letscook.domain.models.FilterLabels
import com.pamn.letscook.domain.models.Recipe

class FilterRecipesUseCase {
    /**
     * Filtra una lista de recetas según los filtros activos.
     */
    operator fun invoke(recipes: List<Recipe>, filters: List<FilterLabels>): List<Recipe> {
        // Obtiene solo los filtros habilitados
        val activeFilters = filters.filter { it.enabled.value }

        // Retorna las recetas que coinciden con todos los filtros habilitados
        return recipes.filter { recipe ->
            activeFilters.all { filter ->
                recipe.appliedFilters.any { it.name == filter.name && it.enabled.value }
            }
        }
    }
}
