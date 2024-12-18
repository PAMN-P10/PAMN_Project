package com.pamn.letscook.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.pamn.letscook.domain.models.DifficultyLevel
import com.pamn.letscook.domain.models.Recipe
import com.pamn.letscook.domain.models.User
import com.pamn.letscook.presentation.viewmodel.RecipeViewModel
import java.time.LocalDateTime

/**
@Composable
fun RecipeListScreen(
    recipeViewModel: RecipeViewModel
) {
    val recipes: State<List<Recipe>> = recipeViewModel.recipes
    val isLoading: State<Boolean> = recipeViewModel.isLoading

    val (searchQuery, setSearchQuery) = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
            .padding(16.dp)
    ) {
        SearchBar(
            searchQuery = searchQuery,
            onSearchQueryChange = {
                setSearchQuery(it)
                recipeViewModel.filterRecipesByName(it)
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading.value) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFF007bff))
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(recipes.value) { recipe ->
                    RecipeCard(recipe = recipe)
                }
            }
        }
    }

    FloatingActionButton(
        onClick = { /* Navigate to favorites */ },
        modifier = Modifier
            .align(Alignment.BottomEnd)
            .padding(24.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_favorite),
            contentDescription = "Favorites",
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun SearchBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(color = Color(0xFFf2f2f2))
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_search),
            contentDescription = "Search",
            tint = Color(0xFF666666),
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        TextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    text = "Search recipes...",
                    color = Color(0xFF666666),
                    fontSize = 16.sp
                )
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
    }
}

@Composable
fun RecipeCard(recipe: Recipe) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(color = Color.White)
            .shadow(elevation = 2.dp, shape = RoundedCornerShape(8.dp))
    ) {
        Column {
            Image(
                painter = painterResource(id = R.drawable.recipe_image),
                contentDescription = "Recipe Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text(
                    text = recipe.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_timer),
                            contentDescription = "Prep Time",
                            tint = Color(0xFF666666),
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "${recipe.preparationTime} min",
                            color = Color(0xFF666666),
                            fontSize = 14.sp
                        )
                    }

                    IconButton(
                        onClick = { /* Toggle favorite */ }
                    ) {
                        Icon(
                            painter = painterResource(id = if (recipe.isFavorite) R.drawable.ic_favorite_filled else R.drawable.ic_favorite_outline),
                            contentDescription = "Favorite",
                            tint = if (recipe.isFavorite) Color.Red else Color(0xFF666666),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FloatingActionButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
            .size(56.dp)
            .clip(RoundedCornerShape(28.dp))
            .background(color = Color(0xFF007bff))
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_favorite),
            contentDescription = "Favorites",
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )
    }
}
        */

import androidx.compose.runtime.getValue
import androidx.compose.ui.text.style.TextOverflow
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PopularRecipesScreen(
    recipeViewModel: RecipeViewModel = viewModel()
) {
    // Trigger recipe initialization when the screen is first loaded
    LaunchedEffect(Unit) {
        recipeViewModel.initializeRecipes()
    }

    val recipes by recipeViewModel.recipes.collectAsState()
    val isLoading by recipeViewModel.isLoading.collectAsState()
    val errorMessage by recipeViewModel.errorMessage.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Popular Recipes") },
                actions = {
                    // Optional: Add refresh button
                    IconButton(onClick = { recipeViewModel.loadRecipes() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh Recipes")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when {
                isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.Center)
                    )
                }
                errorMessage != null -> {
                    Text(
                        text = errorMessage ?: "An error occurred",
                        color = Color.Red,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                recipes.isNotEmpty() -> {
                    RecipesList(recipes = recipes)
                }
                else -> {
                    Text(
                        text = "No recipes available",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

@Composable
fun RecipesList(recipes: List<Recipe>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(recipes) { recipe ->
            RecipeCard(recipe = recipe)
        }
    }
}

@Composable
fun RecipeCard(recipe: Recipe) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            // Recipe image (with placeholder handling)
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(150.dp)
            ) {
                /**
                recipe.mainImage?.let { image ->
                    AsyncImage(
                        model = image.url,
                        contentDescription = recipe.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } ?: Image(
                    painter = painterResource(id = R.drawable.default_recipe_placeholder),
                    contentDescription = recipe.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                */
                // Use a conditional image loading approach
                if (recipe.mainImage?.url?.isNotBlank() == true) {
                    AsyncImage(
                        model = recipe.mainImage?.url,
                        contentDescription = recipe.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    // Fallback to a placeholder color or icon
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.LightGray),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No Image", color = Color.Gray)
                    }
                }
            }

            // Recipe information
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(8.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = recipe.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = recipe.description,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Preparation time
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Preparation time"
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("${recipe.preparationTime} min")
                    }

                    // Difficulty level
                    Text(
                        text = recipe.difficulty.name,
                        style = MaterialTheme.typography.bodySmall,
                        color = when (recipe.difficulty) {
                            DifficultyLevel.Beginner -> Color.Green
                            DifficultyLevel.Intermediate -> Color.Yellow
                            DifficultyLevel.Advanced -> Color.Red
                        }
                    )

                    // Favorite button (placeholder)
                    IconButton(onClick = { /* Add to favorites */ }) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Favorite"
                        )
                    }
                }
            }
        }
    }
}

