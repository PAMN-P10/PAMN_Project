package com.pamn.letscook.domain.models

import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@Stable
class FilterLabels(
    val name: String,
    enabled: Boolean = false,
    val icon: ImageVector? = null
    ){
    val enabled = mutableStateOf(enabled)
}

val filters = listOf(
    FilterLabels(name = "Organic"),
    FilterLabels(name = "Gluten-free"),
    FilterLabels(name = "Dairy-free"),
    FilterLabels(name = "Sweet"),
    FilterLabels(name = "Savory")
)

/**
// Estado de los filtros
private val _filters = MutableStateFlow(listOf(
    FilterLabels(name = "Organic"),
    FilterLabels(name = "Gluten-free"),
    FilterLabels(name = "Dairy-free"),
    FilterLabels(name = "Sweet"),
    FilterLabels(name = "Savory")
))
val filters: StateFlow<List<FilterLabels>> = _filters.asStateFlow()
        */