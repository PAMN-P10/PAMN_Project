package com.pamn.letscook.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.pamn.letscook.domain.models.Ingredient

@Composable
fun IngredientBar(
    ingredients: List<Ingredient>,
    selectedIngredients: List<String>, // Lista de ingredientes seleccionados (por nombre)
    onIngredientSelected: (String) -> Unit // Callback al seleccionar un ingrediente
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
    ) {
        items(ingredients) { ingredient ->
            IngredientChip(
                ingredient = ingredient,
                isSelected = selectedIngredients.contains(ingredient.name),
                onClick = { onIngredientSelected(ingredient.name) }
            )
        }
    }
}

@Composable
private fun IngredientChip(
    ingredient: Ingredient,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(CircleShape)
            .background(
                if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                else MaterialTheme.colorScheme.surface
            )
            .clickable { onClick() }
            .padding(8.dp)
    ) {
        AsyncImage(
            model = ingredient.image?.url ?: "",
            contentDescription = ingredient.name,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = ingredient.name,
            style = MaterialTheme.typography.bodySmall,
            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
        )
    }
}
