package com.pamn.letscook.domain.models

import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.vector.ImageVector

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