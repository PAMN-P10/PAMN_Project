package com.pamn.letscook.presentation.components

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import coil.compose.rememberAsyncImagePainter
import com.pamn.letscook.domain.models.Ingredient

import androidx.compose.ui.text.style.TextAlign


/**
@Composable
fun IngredientRow(viewModel: IngredientViewModel) {
    // Observar estados desde el ViewModel
    val ingredients = viewModel.ingredients.collectAsState()
    val isLoading = viewModel.isLoading.collectAsState()
    val errorMessage = viewModel.errorMessage.collectAsState()

    // Pantalla con lógica de estados
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        when {
            isLoading.value -> {
                CircularProgressIndicator()
            }
            errorMessage.value != null -> {
                Text(
                    text = errorMessage.value ?: "Error desconocido",
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(16.dp)
                )
            }
            ingredients.value.isEmpty() -> {
                Text(
                    text = "No hay ingredientes disponibles.",
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(16.dp)
                )
            }
            else -> {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(ingredients.value) { ingredient ->
                        IngredientCard(ingredient)
                    }
                }
            }
        }
    }
}
@Composable
fun IngredientCard(ingredient: Ingredient) {
    Column(
        modifier = Modifier
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Usa un marcador de posición si la URL de la imagen está vacía
        val imageUrl = if (ingredient.image.url.isNotEmpty()) ingredient.image.url else "https://via.placeholder.com/64"
        val painter = rememberAsyncImagePainter(model = imageUrl)

        Image(
            painter = painter,
            contentDescription = ingredient.name,
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape),
            alignment = Alignment.Center
        )

        // Espaciado y nombre del ingrediente
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = ingredient.name,
            fontSize = 14.sp
        )
    }
}
*/


/**

@Composable
fun IngredientRow(ingredient: Ingredient) {
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Use a placeholder image if the ingredient image URL is empty
        val imageUrl = if (ingredient.image.url.isNotEmpty()) ingredient.image.url else "https://via.placeholder.com/64"

        // Load the image asynchronously
        AsyncImageLoader(
            imageUrl = imageUrl,
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.height(8.dp))
        Text(text = ingredient.name, fontSize = 14.sp)
    }
}
@Composable
fun AsyncImageLoader(
    imageUrl: String,
    modifier: Modifier = Modifier
) {
    var imageState: ImageState by remember{ mutableStateOf(ImageState.Loading) }

    LaunchedEffect(imageUrl) {
        try {
            val painter = rememberAsyncImagePainter(imageUrl)
            imageState = ImageState.Success(painter)
        } catch (e: Exception) {
            imageState = ImageState.Error(e)
        }
    }

    when (imageState) {
        ImageState.Loading -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        is ImageState.Success -> {
            Image(
                painter = (imageState as ImageState.Success).painter,
                contentDescription = null,
                modifier = modifier,
                contentScale = ContentScale.Crop
            )
        }
        is ImageState.Error -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Error loading image")
            }
        }
    }
}

sealed class ImageState {
    object Loading : ImageState()
    data class Success(val painter: Painter) : ImageState()
    data class Error(val error: Exception) : ImageState()
}
*/



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

