package com.pamn.letscook.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pamn.letscook.presentation.viewmodel.IngredientViewModel
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import com.pamn.letscook.domain.models.Ingredient

@Composable
fun IngredientListScreen(viewModel: IngredientViewModel) {
    // Recibe la lista de ingredientes desde el ViewModel
    val ingredients = viewModel.ingredients.collectAsState()

    // Cargar los ingredientes cuando se muestra la pantalla
    LaunchedEffect(Unit) {
        viewModel.loadIngredients()
    }

    // Mostrar la lista de ingredientes o un mensaje si está vacía
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Lista de Ingredientes", fontSize = 24.sp)

        Spacer(modifier = Modifier.height(16.dp))

        if (ingredients.value.isEmpty()) {
            Text(text = "No hay ingredientes disponibles", fontSize = 18.sp)
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(ingredients.value) { ingredient ->
                    IngredientRow(ingredient)
                }
            }
        }
    }
}

@Composable
fun IngredientRow(ingredient: Ingredient) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)) {
        Text(text = "Nombre: ${ingredient.name}", fontSize = 18.sp)
        Text(text = "Tipo: ${ingredient.type.name}", fontSize = 16.sp)
        Text(text = "Cantidad: ${ingredient.quantity}", fontSize = 16.sp)
        Text(text = "¿Es alérgeno?: ${if (ingredient.isAllergen) "Sí" else "No"}", fontSize = 16.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { /* Acción de navegar o realizar alguna operación */ }) {
            Text(text = "Ver más detalles")
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}