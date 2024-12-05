package com.example.pamn_project.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.pamn_project.R

@Composable
fun RecipeForm1Screen(navController: NavHostController) {
    val title = remember { mutableStateOf("") }
    val description = remember { mutableStateOf("") }
    val searchIngredient = remember { mutableStateOf("") }
    val availableIngredients = listOf("Chicken", "Carrot", "Onion", "Garlic", "Tomato")
    val selectedIngredients = remember { mutableStateListOf<Pair<String, MutableState<Pair<Float, String>>>>() }
    val measurementUnits = listOf("kg", "L", "cups", "tbsp", "tsp")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF8E1)) // Pastel yellow
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Title and Description inputs
        TextField(
            value = title.value,
            onValueChange = { title.value = it },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = description.value,
            onValueChange = { description.value = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth()
        )

        // Ingredient search and add
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TextField(
                value = searchIngredient.value,
                onValueChange = { searchIngredient.value = it },
                label = { Text("Search or add ingredient") },
                modifier = Modifier.weight(1f)
            )
            Button(onClick = {
                if (searchIngredient.value.isNotBlank()) {
                    selectedIngredients.add(Pair(searchIngredient.value, mutableStateOf(Pair(0f, "kg"))))
                    searchIngredient.value = ""
                }
            }) {
                Text("Add")
            }
        }

        // Show selected ingredients with DropdownMenu for measurement unit
        Text("Selected Ingredients:", style = MaterialTheme.typography.bodyMedium)
        selectedIngredients.forEachIndexed { index, ingredient ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                // Ingredient Name
                Text(ingredient.first, Modifier.weight(1f))

                // Quantity Input
                TextField(
                    value = ingredient.second.value.first.toString(),
                    onValueChange = {
                        val newQuantity = it.toFloatOrNull() ?: 0f
                        ingredient.second.value = ingredient.second.value.copy(first = newQuantity)
                    },
                    modifier = Modifier.width(80.dp),
                    label = { Text("Qty") }
                )

                // Dropdown for Unit Selection
                var expanded by remember { mutableStateOf(false) }
                Box(modifier = Modifier.wrapContentSize(Alignment.TopStart)) {
                    Button(
                        onClick = { expanded = true },
                        modifier = Modifier.width(100.dp)
                    ) {
                        Text(ingredient.second.value.second)
                    }
                    DropdownMenu(
                        expanded = true, // Puedes reemplazar esto con tu lógica para mostrar el menú
                        onDismissRequest = { /* Acción para cerrar el menú */ },
                        modifier = Modifier.wrapContentSize()
                    ) {
                        listOf("kg", "L", "cups").forEach { unit ->
                            DropdownMenuItem(
                                text = { Text(unit) }, // El contenido del menú debe ir aquí
                                onClick = { /* Acción al seleccionar la unidad */ }
                            )
                        }
                    }

                }
            }
        }

        Spacer(Modifier.height(8.dp))

        // Next button
        Button(
            onClick = { navController.navigate("recipe_form_step2") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Next")
        }

        // Step indicator
        Text("Step 1/2", Modifier.align(Alignment.CenterHorizontally))
    }
}
