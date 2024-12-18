package com.pamn.letscook.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp

import com.pamn.letscook.presentation.components.IngredientRow
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import com.pamn.letscook.presentation.viewmodel.IngredientViewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.pamn.letscook.data.repositories.IngredientRepository
import com.pamn.letscook.domain.models.Image
import com.pamn.letscook.domain.models.Ingredient
import com.pamn.letscook.domain.models.IngredientType
import kotlinx.coroutines.launch

// Screen de prueba
@Composable
fun HomeScreen(
    viewModel: IngredientViewModel,
    repository: IngredientRepository,
) {

    /*
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {

        IngredientDatabaseTest(ingredient)
    }
*/


/**

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
fun IngredientDatabaseTest(repository: IngredientRepository) {
    // Estado para mostrar el nombre del ingrediente
    val ingredientName = remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    val ingredient = Ingredient(
        name = "Tomato",
        type = IngredientType.Vegetable,
        quantity = 0.0,
        isAllergen = false,
        image = Image(
            label = "Tomato",
            url = "data:image/webp;base64,..."
        )
    )

    // Guardar e interactuar con la base de datos dentro de una corrutina
    LaunchedEffect(Unit) {
        // Guardamos el ingrediente en la base de datos
        repository.saveIngredient(ingredient)

        // Leemos el ingrediente por su nombre
        val result = repository.getIngredientByName("Tomato")

        // Verificamos si la operación fue exitosa y obtenemos el nombre
        ingredientName.value = if (result.isSuccess) {
            result.getOrNull()?.name ?: "Ingredient not found"
        } else {
            "Failed to load ingredient"
        }
    }

    // Mostrar el nombre del ingrediente
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Ingredient Name: ${ingredientName.value}")
    }
    */
}
