package com.pamn.letscook.presentation.screens

import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.gson.Gson
import com.pamn.letscook.domain.models.Ingredient
import com.pamn.letscook.domain.models.MeasurementUnit
import com.pamn.letscook.domain.models.Recipe
import com.pamn.letscook.presentation.components.TopBar
import com.pamn.letscook.presentation.viewmodel.RecipeViewModel

@Composable
fun RecipeForm1Screen(navController: NavHostController, recipeViewModel: RecipeViewModel) {
    var searchIngredient by remember { mutableStateOf("") }
    val availableIngredients = remember { mutableStateListOf("Chicken", "Carrot", "Onion", "Garlic", "Tomato", "Chocolate") }
    val filteredIngredients = availableIngredients.filter { it.startsWith(searchIngredient, ignoreCase = true) }
    var selectedIngredient by remember { mutableStateOf<String?>(null) }
    val scrollState = rememberScrollState()
    var quantity by remember { mutableStateOf(1.0) }

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    val user = recipeViewModel.user
    var unit by remember { mutableStateOf(MeasurementUnit.GRAMS) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp)),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        TopBar(title = "Upload recipe", onBackClick = {})
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(width = 1.dp, color = MaterialTheme.colorScheme.primary, shape = MaterialTheme.shapes.medium)
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colorScheme.onPrimary)
        ) {
            BasicTextField(
                value = recipeViewModel.title, // Replace this with the title state variable
                onValueChange = { recipeViewModel.title = it }, // Handle title change
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(3.dp),
                textStyle = TextStyle(fontSize = 16.sp, color = MaterialTheme.colorScheme.primary),
                decorationBox = { innerTextField ->
                    if (recipeViewModel.title.isEmpty()) {
                        Text("Title", fontSize = 16.sp, color = Color.Gray)
                    }
                    innerTextField()
                }
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .width(200.dp)
                    .height(120.dp)
                    .border(width = 1.dp, color = MaterialTheme.colorScheme.primary, shape = MaterialTheme.shapes.medium)
                    .clip(MaterialTheme.shapes.medium)
                    .verticalScroll(scrollState)
                    .background(MaterialTheme.colorScheme.onPrimary)
            ) {
                BasicTextField(
                    value = recipeViewModel.description,
                    onValueChange = { recipeViewModel.description = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(3.dp),
                    textStyle = TextStyle(fontSize = 16.sp, color = MaterialTheme.colorScheme.primary),
                    decorationBox = { innerTextField ->
                        if (recipeViewModel.description.isEmpty()) {
                            Text("Description...", fontSize = 16.sp, color = Color.Gray)
                        }
                        innerTextField()
                    }
                )
            }
            Spacer(modifier = Modifier.width(2.dp))
            Column(
                modifier = Modifier
                    .width(190.dp)
                    .height(120.dp)
            ) {
                var expanded by remember { mutableStateOf(false) }
                var selectedOption by remember { mutableStateOf("Select Difficulty") }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(width = 1.dp, color = MaterialTheme.colorScheme.primary, shape = MaterialTheme.shapes.small)
                        .background(MaterialTheme.colorScheme.onPrimary)
                        .clip(MaterialTheme.shapes.small),
                ) {
                    Text(
                        text = selectedOption ?: "Select Difficulty",
                        modifier = Modifier
                            .clickable { expanded = true }
                            .padding(1.dp)
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                        color = MaterialTheme.colorScheme.primary
                    )
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        listOf("Beginner", "Intermediate", "Advanced").forEach { level ->
                            DropdownMenuItem(
                                text = { Text(level) },
                                onClick = {
                                    selectedOption = level
                                    expanded = false
                                }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                var expanded2 by remember { mutableStateOf(false) }
                var selectedOptions2  = remember { mutableStateListOf<String>() }
                val allergens = listOf(
                    "Celery", "Cereals", "Crustaceans", "Eggs", "Fish", "Lupin", "Milk",
                    "Molluscs", "Mustard", "Nuts", "Peanuts", "Sesame seeds", "Soya", "Sulphites"
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(width = 1.dp, color = MaterialTheme.colorScheme.primary, shape = MaterialTheme.shapes.small)
                        .background(MaterialTheme.colorScheme.onPrimary)
                        .clip(MaterialTheme.shapes.small),
                ) {
                    Text(
                        text = if (selectedOptions2.isEmpty()) "Select Allergens" else "${selectedOptions2.size} selected",
                        modifier = Modifier
                            .clickable { expanded2 = true }
                            .padding(1.dp)
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                        color = MaterialTheme.colorScheme.primary
                    )
                    DropdownMenu(expanded = expanded2, onDismissRequest = { expanded2 = false }) {
                        allergens.forEach { allergen ->
                            DropdownMenuItem(
                                text = {
                                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                                        Text(allergen)
                                        Spacer(Modifier.weight(1f))
                                        if (allergen in selectedOptions2) Text("✔", color = MaterialTheme.colorScheme.primary)
                                    }
                                },
                                onClick = {
                                    if (allergen in selectedOptions2) selectedOptions2.remove(allergen)
                                    else selectedOptions2.add(allergen)
                                }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                var expanded3 by remember { mutableStateOf(false) }
                var selectedOptions3  = remember { mutableStateListOf<String>() }
                val type = listOf(
                    "Organic", "Gluten-free", "Dairy-free", "Sweet", "Savory",
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(width = 1.dp, color = MaterialTheme.colorScheme.primary, shape = MaterialTheme.shapes.small)
                        .background(MaterialTheme.colorScheme.onPrimary)
                        .clip(MaterialTheme.shapes.small),
                ) {
                    Text(
                        text = if (selectedOptions3.isEmpty()) "Select Type of Food" else "${selectedOptions3.size} selected",
                        modifier = Modifier
                            .clickable { expanded3 = true }
                            .padding(1.dp)
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                        color = MaterialTheme.colorScheme.primary
                    )
                    DropdownMenu(expanded = expanded3, onDismissRequest = { expanded3 = false }) {
                        allergens.forEach { allergen ->
                            DropdownMenuItem(
                                text = {
                                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                                        Text(allergen)
                                        Spacer(Modifier.weight(1f))
                                        if (allergen in selectedOptions3) Text("✔", color = MaterialTheme.colorScheme.primary)
                                    }
                                },
                                onClick = {
                                    if (allergen in selectedOptions3) selectedOptions3.remove(allergen)
                                    else selectedOptions3.add(allergen)
                                }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                //Text(text = "Approximate duration:", color = MaterialTheme.colorScheme.primary, fontSize = 12.sp,  modifier = Modifier.align(Alignment.Start))
                var timeText by remember { mutableStateOf("") }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .border(width = 1.dp, color = MaterialTheme.colorScheme.primary, shape = MaterialTheme.shapes.small)
                        .background(MaterialTheme.colorScheme.onPrimary)
                        .clip(MaterialTheme.shapes.small)
                ) {
                    BasicTextField(
                        value = timeText,
                        onValueChange = { timeText = it },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        textStyle = TextStyle(fontSize = 16.sp, color = MaterialTheme.colorScheme.primary),
                        decorationBox = { innerTextField ->
                            // Combine "Prep Time: " with the input text, or default to placeholder
                            if (timeText.isEmpty()) {
                                Text(
                                    text = "Prep Time: HH:MM:SS",
                                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                                )
                            }
                            innerTextField()
                        }
                    )
                }
            }
        }
        Column(
            modifier = Modifier
                .height(260.dp)
                .border(width = 1.dp, color = MaterialTheme.colorScheme.primary, shape = MaterialTheme.shapes.medium)
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colorScheme.onPrimary)
        ) {
            TextField(
                value = searchIngredient,
                onValueChange = { searchIngredient = it },
                label = { Text("Search or add an ingredient") },
                modifier = Modifier
                    .fillMaxWidth()
                    .border(width = 1.dp, color = MaterialTheme.colorScheme.primary, shape = MaterialTheme.shapes.medium),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    filteredIngredients.forEach { ingredient ->
                        val isSelected = ingredient == selectedIngredient
                        Text(
                            text = ingredient,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedIngredient = ingredient }
                                .background(if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f) else Color.Transparent),
                        )
                    }
                    if (filteredIngredients.isEmpty()) {
                        Text(
                            text = "No ingredients found. Add \"$searchIngredient\"?",
                        )
                    }
                }
            }
            Button(
                onClick = {
                    if (searchIngredient.isNotBlank() && !availableIngredients.contains(searchIngredient)) {
                        availableIngredients.add(searchIngredient)
                    }
                    selectedIngredient?.let {
                        recipeViewModel.addIngredient(it, quantity, "kg")
                    }
                    searchIngredient = ""
                    selectedIngredient = null
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.medium)
                    .background(MaterialTheme.colorScheme.secondary)
                    .border(width = 1.dp, color = MaterialTheme.colorScheme.primary, shape = MaterialTheme.shapes.medium)
            ) {
                Text("Add", color = MaterialTheme.colorScheme.primary)
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colorScheme.onPrimary)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = MaterialTheme.shapes.medium
                ),
        ) {
            Text(
                text = "Selected Ingredients:",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(start = 16.dp)
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                items(recipeViewModel.selectedIngredients.size) { index ->
                    val (ingredientName, ingredientQuantity, unit) = recipeViewModel.selectedIngredients[index]
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.onPrimary)
                            .border(width = 1.dp, color = MaterialTheme.colorScheme.primary, shape = MaterialTheme.shapes.small)
                            .clip(MaterialTheme.shapes.small)
                    ) {
                        Text(text = ingredientName, modifier = Modifier.weight(1f), color = MaterialTheme.colorScheme.primary)

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .width(80.dp)
                                .height(40.dp)
                                .border(1.dp, color = MaterialTheme.colorScheme.primary, MaterialTheme.shapes.small)
                                .background(color = MaterialTheme.colorScheme.onPrimary)
                        ) {
                            BasicTextField(
                                value = "%.1f".format(ingredientQuantity),
                                onValueChange = { input ->
                                    input.toDoubleOrNull()?.let {
                                        recipeViewModel.selectedIngredients[index] =
                                            Triple(ingredientName, it, unit)
                                    }
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight(),
                                textStyle = LocalTextStyle.current.copy(
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colorScheme.onSurface
                                ),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                decorationBox = { innerTextField ->
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(0.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        innerTextField()
                                    }
                                }
                            )
                            Column(
                                modifier = Modifier.fillMaxHeight(),
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                IconButton(
                                    onClick = {
                                        val currentQuantity = recipeViewModel.selectedIngredients[index].second
                                        recipeViewModel.selectedIngredients[index] =
                                            Triple(ingredientName, currentQuantity + 1.0, unit)
                                    },
                                    modifier = Modifier.size(16.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.KeyboardArrowUp,
                                        contentDescription = "Increase",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                                IconButton(
                                    onClick = {
                                        val currentQuantity = recipeViewModel.selectedIngredients[index].second
                                        if (currentQuantity > 0) {
                                            recipeViewModel.selectedIngredients[index] =
                                                Triple(ingredientName, currentQuantity - 1.0, unit)
                                        }
                                    },
                                    modifier = Modifier.size(16.dp),
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.KeyboardArrowDown,
                                        contentDescription = "Decrease",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                        var expanded by remember { mutableStateOf(false) }
                        var unit by remember { mutableStateOf(MeasurementUnit.GRAMS) } // Default value is MeasurementUnit.GRAMS
                        Box(
                            modifier = Modifier
                                .width(80.dp)
                                .height(40.dp)
                                .border(width = 1.dp, color = MaterialTheme.colorScheme.primary, shape = MaterialTheme.shapes.small)
                                .background(MaterialTheme.colorScheme.onPrimary)
                                .clip(MaterialTheme.shapes.small)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clickable { expanded = !expanded }
                                    .background(MaterialTheme.colorScheme.onPrimary),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = unit.displayName, // Use the displayName of the MeasurementUnit enum to display the selected unit
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.weight(1f),
                                    textAlign = TextAlign.Center
                                )
                                Icon(
                                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }

                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false },
                                modifier = Modifier
                                    .wrapContentSize()
                                    .background(MaterialTheme.colorScheme.onPrimary)
                                    .border(width = 1.dp, color = MaterialTheme.colorScheme.primary, shape = MaterialTheme.shapes.medium)
                                    .clip(MaterialTheme.shapes.medium)
                            ) {
                                MeasurementUnit.values().forEach { unitOption ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                text = unitOption.displayName, // Use the displayName for display in the dropdown
                                                color = MaterialTheme.colorScheme.primary
                                            )
                                        },
                                        onClick = {
                                            // Update the unit with the MeasurementUnit enum value
                                            unit = unitOption // Now we store the MeasurementUnit enum directly
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }
                        IconButton(
                            onClick = {
                                recipeViewModel.removeIngredient(ingredientName)
                            },
                            modifier = Modifier.size(30.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Remove ingredient",
                                tint = Color.Red
                            )
                        }
                    }
                }
            }
        }
        Spacer(Modifier.height(2.dp))
        Button(
            onClick = {
                if (recipeViewModel.title.isNotEmpty() && recipeViewModel.description.isNotEmpty()) {
                    // Construir la receta para pasarla a RecipeForm2
                    val recipe = Recipe(
                        title = recipeViewModel.title,
                        description = recipeViewModel.description,
                        author = user,
                        difficulty = recipeViewModel.selectedDifficulty,
                        preparationTime = recipeViewModel.prepTime,  // asume que este dato está recogido en el ViewModel
                        allergens = recipeViewModel.selectedAllergens,
                        ingredients = recipeViewModel.selectedIngredients.map { ingredient ->
                            Ingredient(
                                name = ingredient.first,
                                quantity = ingredient.second,
                                unit = unit,
                                type = TODO(),
                                isAllergen = TODO(),
                                image = TODO()
                            )
                        },
                        steps = emptyList()  // Empty for now, we fill this in RecipeForm2
                    )
                    val ingredientJson = Gson().toJson(recipe)  // Serialize the `Ingredient` object to JSON
                    navController.navigate("recipe_form2_screen/$ingredientJson")
                }
            },
            modifier = Modifier
                .shadow(8.dp, shape = CircleShape)
                .fillMaxWidth()
                .defaultMinSize(minHeight = 48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Text(
                text = "Next",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelLarge
            )
        }
        Text("1/2", Modifier.align(Alignment.CenterHorizontally))
    }
}

fun Ingredient.toJson(): String {
    return Gson().toJson(this)
}