package com.pamn.letscook.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pamn.letscook.domain.models.DifficultyLevel
import com.pamn.letscook.domain.models.Recipe
import com.pamn.letscook.presentation.viewmodel.RecipeViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.pamn.letscook.domain.models.filters
import com.pamn.letscook.presentation.components.FilterBar
import com.pamn.letscook.presentation.components.IngredientBar
import com.pamn.letscook.presentation.components.SearchBar
import com.pamn.letscook.presentation.viewmodel.IngredientViewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.pamn.letscook.presentation.components.FooterNavigation
import com.pamn.letscook.presentation.viewmodel.RecipeFilterViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PopularRecipesScreen(
    recipeViewModel: RecipeViewModel = viewModel(),
    ingredientViewModel: IngredientViewModel = viewModel(),
    recipeFilterViewModel: RecipeFilterViewModel = viewModel(),
    navController: NavController,
    ) {
    var searchQuery by remember { mutableStateOf("") }
    val filteredRecipes by recipeFilterViewModel.filteredRecipes.collectAsState()

    // Trigger recipe initialization when the screen is first loaded
    LaunchedEffect(Unit) {
        recipeViewModel.initializeRecipes()
        ingredientViewModel.initializeIngredients()
    }

    val recipes by recipeViewModel.recipes.collectAsState()
    val ingredients by ingredientViewModel.ingredients.collectAsState()
    val selectedIngredients by ingredientViewModel.selectedIngredients.collectAsState()
    val filters by recipeFilterViewModel.filters.collectAsState()
    val isLoading by recipeViewModel.isLoading.collectAsState()
    val errorMessage by recipeViewModel.errorMessage.collectAsState()

    // Filtrar recetas cada vez que cambien los filtros o las recetas
    LaunchedEffect(filters, recipes) {
        recipeFilterViewModel.updateFilters(filters, recipes)
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Popular Recipes") },
                actions = {
                    IconButton(onClick = { recipeViewModel.loadRecipes() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh Recipes")
                    }
                }
            )
        },
        bottomBar = {
            FooterNavigation(
                onHeartClick = {  navController.navigate("tem_home_screen") },
                onAddClick = { navController.navigate("recipeform1_screen") },
                onProfileClick = { navController.navigate("profile_screen") },
                modifier = Modifier.navigationBarsPadding() // Respeta la barra de navegación del sistema
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                SearchBar(
                    searchText = searchQuery,  // Pass the string value directly
                    onSearchTextChange = { query ->
                        searchQuery = query  // Update the state directly
                        recipeViewModel.filterRecipesByName(query)
                    },
                    onSearchVoiceInput = { voiceQuery ->
                        searchQuery = voiceQuery  // Update the state directly
                        recipeViewModel.filterRecipesByName(voiceQuery)
                    }
                )

                Spacer(modifier = Modifier.height(5.dp))
                IngredientBar(
                    ingredients = ingredients,
                    selectedIngredients = selectedIngredients,
                    onIngredientSelected = { ingredientViewModel.toggleIngredientSelection(it) }
                )

                Spacer(modifier = Modifier.height(2.dp))

                FilterBar(
                    filter = filters,
                    onShowFilters = {
                        // Mostrar un diálogo o manejar filtros adicionales
                    },
                    onFilterChange = {
                        recipeFilterViewModel.updateFilters(filters, recipes)
                    }
                )

                /**Box(
                    modifier = Modifier.fillMaxSize()
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
                }*/
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                    )
                } else if (errorMessage != null) {
                    Text(
                        text = errorMessage ?: "An error occurred",
                        color = Color.Red,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                } else if (recipes.isNotEmpty()) {
                    RecipesList(recipes = recipes) { recipeTitle ->
                        navController.navigate("RecipeDetail/$recipeTitle")
                    }
                } else {
                    Text(
                        text = "No recipes available",
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    )
}

@Composable
fun RecipesList(recipes: List<Recipe>, onRecipeClick: (String) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(recipes) { recipe ->
            RecipeCard(recipe = recipe, onClick = { onRecipeClick(recipe.title) })
        }
    }
}


@Composable
fun RecipeCard(recipe: Recipe, onClick: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(recipe.title) },
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
                val imageUrl = recipe.mainImage?.url ?: recipe.ingredients.firstOrNull()?.image?.url
                if (!imageUrl.isNullOrBlank()) {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = recipe.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize(),

                    )

                } else {
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

