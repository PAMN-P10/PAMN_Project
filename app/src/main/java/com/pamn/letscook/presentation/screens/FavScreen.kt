package com.pamn.letscook.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pamn.letscook.R
import androidx.compose.ui.Alignment
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.pamn.letscook.presentation.components.FooterNavigation
import com.pamn.letscook.presentation.viewmodel.RecipeViewModel
import com.pamn.letscook.presentation.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavScreen(
    userViewModel: UserViewModel = viewModel(),
    recipeViewModel: RecipeViewModel = viewModel(),
    navController: NavController
) {
    val userId = userViewModel.currentUserId
    val favoriteRecipes by recipeViewModel.recipes.collectAsState(initial = emptyList())

    LaunchedEffect(userId) {
        userId?.let { recipeViewModel.loadFavoritesFromFirestore(it) }
    }

    val filteredFavorites = favoriteRecipes.filter { it.isFavorite }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("recipeList") }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            modifier = Modifier.size(24.dp),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "My Favourites",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
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
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                // Imagen de fondo decorativa
                Image(
                    painter = painterResource(id = R.drawable.love_cooking),
                    contentDescription = "Background Decoration",
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 16.dp),
                    contentScale = ContentScale.Fit
                )

                // Contenido principal
                if (filteredFavorites.isEmpty()) {
                    Text(
                        text = "No favorite recipes",
                        modifier = Modifier
                            .fillMaxSize()
                            .wrapContentSize(Alignment.Center)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(filteredFavorites) { recipe ->
                            RecipeCard(
                                recipe = recipe,
                                onClick = { navController.navigate("RecipeDetail/${recipe.title}") },
                                onFavoriteClick = {
                                    userId?.let { recipeViewModel.toggleFavorite(recipe, it) }
                                }
                            )
                        }
                    }
                }
            }
        }
    )
}