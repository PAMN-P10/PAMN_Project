package com.example.pamn_project.screens

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.pamn_project.ui.components.TopBar
import com.example.pamn_project.viewmodel.RecipeViewModel
import java.io.ByteArrayOutputStream
import java.io.InputStream

@Composable
fun RecipeForm1Screen(navController: NavHostController, recipeViewModel: RecipeViewModel) {
    var searchIngredient by remember { mutableStateOf("") }
    val availableIngredients = remember { mutableStateListOf("Chicken", "Carrot", "Onion", "Garlic", "Tomato", "Chocolate") }
    val filteredIngredients = availableIngredients.filter { it.startsWith(searchIngredient, ignoreCase = true) }
    var selectedIngredient by remember { mutableStateOf<String?>(null) }
    val scrollState = rememberScrollState()
    val context = LocalContext.current as Activity

    var bitmap by remember { mutableStateOf<android.graphics.Bitmap?>(null) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                recipeViewModel.selectedImageUri = uri
                val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
                bitmap = BitmapFactory.decodeStream(inputStream)
            }
        }
    }
    var quantity by remember { mutableStateOf(1.0) }

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
                .border(width = 1.dp, color = Color.Black, shape = MaterialTheme.shapes.medium)
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colorScheme.onPrimary)
        ) {
            TextField(
                value = recipeViewModel.title,
                onValueChange = { newValue -> recipeViewModel.title = newValue },
                label = { Text("Title") },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent,
                )
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .width(200.dp)
                    .height(100.dp)
                    .border(width = 1.dp, color = Color.Black, shape = MaterialTheme.shapes.medium)
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
                    textStyle = androidx.compose.ui.text.TextStyle(fontSize = 16.sp),
                    decorationBox = { innerTextField ->
                        if (recipeViewModel.description.isEmpty()) {
                            Text("Description...", fontSize = 16.sp, color = androidx.compose.ui.graphics.Color.Gray)
                        }
                        innerTextField()
                    }
                )
            }
            Box(
                modifier = Modifier
                    .width(150.dp)
                    .height(100.dp)
                    .border(width = 1.dp, color = Color.Black, shape = MaterialTheme.shapes.medium)
                    .clip(MaterialTheme.shapes.medium)
                    .background(MaterialTheme.colorScheme.onPrimary)
                    .clickable {
                        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                        launcher.launch(intent)
                    },
                contentAlignment = Alignment.Center
            ) {
                if (bitmap != null) {
                    Image(
                        bitmap = bitmap!!.asImageBitmap(),
                        contentDescription = "Uploaded Image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Upload Icon",
                            tint = Color.Black,
                            modifier = Modifier.size(50.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Upload Photo",
                            color = Color.Black,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
        Column(
            modifier = Modifier
                .height(200.dp)
                .border(width = 1.dp, color = Color.Black, shape = MaterialTheme.shapes.medium)
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colorScheme.onPrimary)
        ) {
            TextField(
                value = searchIngredient,
                onValueChange = { searchIngredient = it },
                label = { Text("Search or add an ingredient") },
                modifier = Modifier
                    .fillMaxWidth()
                    .border(width = 1.dp, color = Color.Black, shape = MaterialTheme.shapes.medium),
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
                    .border(width = 1.dp, color = Color.Black, shape = MaterialTheme.shapes.medium)
            ) {
                Text("Add", color = MaterialTheme.colorScheme.primary)
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colorScheme.onPrimary)
                .border(
                    width = 1.dp,
                    color = Color.Black,
                    shape = MaterialTheme.shapes.medium
                ),
        ) {
            Text(
                text = "Selected Ingredients:",
                style = MaterialTheme.typography.bodyMedium,
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
                            .border(width = 1.dp, color = Color.Black, shape = MaterialTheme.shapes.small)
                            .clip(MaterialTheme.shapes.small)
                    ) {
                        Text(text = ingredientName, modifier = Modifier.weight(1f))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .width(80.dp)
                                .height(40.dp)
                                .border(1.dp, Color.Black, MaterialTheme.shapes.small)
                                .background(Color.White)
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
                                        contentDescription = "Increase"
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
                                    modifier = Modifier.size(16.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.KeyboardArrowDown,
                                        contentDescription = "Decrease"
                                    )
                                }
                            }
                        }
                        var expanded by remember { mutableStateOf(false) }
                        Box(
                            modifier = Modifier
                                .width(80.dp)
                                .height(40.dp)
                                .border(width = 1.dp, color = Color.Black, shape = MaterialTheme.shapes.small)
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
                                    text = unit,
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
                                    .border(width = 1.dp, color = Color.Black, shape = MaterialTheme.shapes.medium)
                                    .clip(MaterialTheme.shapes.medium)
                            ) {
                                listOf("kg", "L", "cups", "tbsp", "tsp").forEach { unitOption ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                text = unitOption,
                                                color = MaterialTheme.colorScheme.primary
                                            )
                                        },
                                        onClick = {
                                            recipeViewModel.selectedIngredients[index] =
                                                Triple(ingredientName, ingredientQuantity, unitOption)
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
                // Validamos que los campos requeridos estén completos antes de navegar
                recipeViewModel.title.isNotBlank() &&
                recipeViewModel.description.isNotBlank() &&
                recipeViewModel.selectedImageUri != null &&
                recipeViewModel.selectedIngredients.isNotEmpty()
                // Convertimos la URI a String
                val imageBase64 = encodeImageToBase64(recipeViewModel.selectedImageUri!!)
                // Construimos los parámetros para la navegación
                val ingredientsList = recipeViewModel.selectedIngredients.joinToString(",")
                Log.d("RecipeForm1Screen", "Title: ${recipeViewModel.title}")
                Log.d("RecipeForm1Screen", "Description: ${recipeViewModel.description}")
                Log.d("RecipeForm1Screen", "Image URI: $imageBase64")
                Log.d("RecipeForm1Screen", "Ingredients: $ingredientsList")
                navController.navigate(
                    "recipe_form_2_screen/${recipeViewModel.title}/${recipeViewModel.description}/$imageBase64/$ingredientsList"
                )

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

        Text("1/2")//, Modifier.align(Alignment.CenterHorizontally))
    }
}

@Composable
fun encodeImageToBase64(uri: Uri): String? {
    val context = LocalContext.current
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        Base64.encodeToString(byteArray, Base64.DEFAULT)
    } catch (e: Exception) {
        null
    }
}
