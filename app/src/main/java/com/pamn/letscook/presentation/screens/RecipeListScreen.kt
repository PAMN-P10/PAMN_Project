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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.pamn.letscook.domain.models.DifficultyLevel
import com.pamn.letscook.domain.models.Recipe
import com.pamn.letscook.presentation.components.FilterBar
import com.pamn.letscook.presentation.components.IngredientBar
import com.pamn.letscook.presentation.components.SearchBar
import com.pamn.letscook.presentation.components.FooterNavigation
import com.pamn.letscook.presentation.viewmodel.IngredientViewModel
import com.pamn.letscook.presentation.viewmodel.RecipeFilterViewModel
import com.pamn.letscook.presentation.viewmodel.RecipeViewModel
import com.pamn.letscook.presentation.viewmodel.UserViewModel

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

    LaunchedEffect(Unit) {
        /*recipeViewModel.initializeRecipes()
        ingredientViewModel.initializeIngredients()
        recipeViewModel.loadFavoritesFromDatabase()*/
    }

    val recipes by recipeViewModel.recipes.collectAsState()
    val ingredients by ingredientViewModel.ingredients.collectAsState()
    val selectedIngredients by ingredientViewModel.selectedIngredients.collectAsState()
    val filters by recipeFilterViewModel.filters.collectAsState()
    val isLoading by recipeViewModel.isLoading.collectAsState()
    val errorMessage by recipeViewModel.errorMessage.collectAsState()

    LaunchedEffect(filters, recipes) {
        recipeFilterViewModel.updateFilters(filters, recipes)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Popular Recipes") },
                actions = {
                    /*IconButton(onClick = { recipeViewModel.loadRecipes() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh Recipes")
                    }*/
                }
            )
        },
        bottomBar = {
            FooterNavigation(
                onHeartClick = { navController.navigate("fav_screen") },
                onAddClick = { navController.navigate("recipeform1_screen") },
                onProfileClick = { navController.navigate("profile_screen") },
                modifier = Modifier.navigationBarsPadding()
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                SearchBar(
                    searchText = searchQuery,
                    onSearchTextChange = { query ->
                        searchQuery = query
                        //recipeViewModel.filterRecipesByName(query)
                    },
                    onSearchVoiceInput = { voiceQuery ->
                        searchQuery = voiceQuery
                        //recipeViewModel.filterRecipesByName(voiceQuery)
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
                    onShowFilters = {},
                    onFilterChange = {
                        recipeFilterViewModel.updateFilters(filters, recipes)
                    }
                )

                when {
                    isLoading -> CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    errorMessage != null -> Text(
                        text = errorMessage ?: "An error occurred",
                        color = Color.Red,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    recipes.isNotEmpty() -> RecipesList(
                        recipes = recipes,
                        onRecipeClick = { recipeTitle -> navController.navigate("RecipeDetail/$recipeTitle") },
                        onFavoriteClick = { recipe -> recipeViewModel.toggleFavorite(recipe) }
                    )
                    else -> Text(
                        text = "No recipes available",
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    )
}

@Composable
fun RecipesList(
    recipes: List<Recipe>,
    onRecipeClick: (String) -> Unit,
    onFavoriteClick: (Recipe) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(recipes) { recipe ->
            RecipeCard(
                recipe = recipe,
                onClick = { onRecipeClick(recipe.title) },
                onFavoriteClick = onFavoriteClick
            )
        }
    }
}

@Composable
fun RecipeCard(
    recipe: Recipe,
    onClick: (String) -> Unit,
    onFavoriteClick: (Recipe) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(recipe.title) },
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(150.dp)
            ) {
                val imageUrl = recipe.mainImage?.url ?: recipe.ingredients.firstOrNull()?.image?.url
                if (!imageUrl.isNullOrBlank()) {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = recipe.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
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
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Preparation time"
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("${recipe.preparationTime} min")
                    }

                    /*Text(
                        text = recipe.difficulty.name,
                        style = MaterialTheme.typography.bodySmall,
                        color = when (recipe.difficulty) {
                            DifficultyLevel.Beginner -> Color.Green
                            DifficultyLevel.Intermediate -> Color.Yellow
                            DifficultyLevel.Advanced -> Color.Red
                        }
                    )*/

                    IconButton(onClick = { onFavoriteClick(recipe) }) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Favorite",
                            tint = if (recipe.isFavorite) Color.Red else Color.Gray
                        )
                    }
                }
            }
        }
    }
}