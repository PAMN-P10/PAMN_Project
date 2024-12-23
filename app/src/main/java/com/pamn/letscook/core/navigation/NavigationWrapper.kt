package com.pamn.letscook.core.navigation

import android.net.Uri
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import com.pamn.letscook.data.repositories.IngredientRepository
import com.pamn.letscook.data.repositories.RecipeInitializer
import com.pamn.letscook.data.repositories.RecipeRepository
import com.pamn.letscook.domain.models.Recipe
import com.pamn.letscook.domain.usecases.IngredientInitializer
import com.pamn.letscook.presentation.components.IngredientListScreen
import com.pamn.letscook.presentation.screens.RecipeDetailScreen
import com.pamn.letscook.presentation.screens.PopularRecipesScreen
import com.pamn.letscook.presentation.viewmodel.IngredientViewModel
import com.pamn.letscook.presentation.viewmodel.IngredientViewModelFactory
import com.pamn.letscook.presentation.viewmodel.RecipeViewModel
import com.pamn.letscook.presentation.viewmodel.RecipeViewModelFactory
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.pamn.letscook.presentation.screens.FavScreen
import com.pamn.letscook.presentation.screens.LoginScreen
import com.pamn.letscook.presentation.screens.ProfileScreen
import com.pamn.letscook.presentation.screens.RecipeForm1Screen
import com.pamn.letscook.presentation.screens.RecipeForm2Screen
import com.pamn.letscook.presentation.screens.SignUp1Screen
import com.pamn.letscook.presentation.screens.SignUp2Screen
import com.pamn.letscook.presentation.screens.WelcomeOptionsScreen
import com.pamn.letscook.presentation.screens.WelcomeScreen
import com.pamn.letscook.presentation.viewmodel.RecipeFilterViewModel
import com.pamn.letscook.presentation.viewmodel.UserViewModel

@Composable
fun NavigationWrapper(
    ingredientRepository: IngredientRepository,
    ingredientInitializer: IngredientInitializer,
    recipeRepository: RecipeRepository,
    recipeInitializer: RecipeInitializer
) {
    val navHostController = rememberNavController()

    val recipeFactory = RecipeViewModelFactory(
        recipeRepository = recipeRepository,
        recipeInitializer = recipeInitializer
    )
    val recipeViewModel: RecipeViewModel = viewModel(factory = recipeFactory)

    val ingredientFactory = IngredientViewModelFactory(
        ingredientRepository = ingredientRepository,
        ingredientInitializer = ingredientInitializer
    )
    val ingredientViewModel: IngredientViewModel = viewModel(factory = ingredientFactory)

    val recipeFilterViewModel: RecipeFilterViewModel = viewModel()

    val userViewModel: UserViewModel = viewModel()

    val userData = remember { mutableStateOf(mutableMapOf<String, String>()) }

    NavHost(
        navController = navHostController,
       // startDestination = "welcome_screen"
        startDestination = if (userViewModel.currentUser.value != null) "fav_screen" else "welcome_screen"
    ) {
        composable("welcome_screen") {
            WelcomeScreen(navController = navHostController)
        }
        composable("welcome_options_screen") {
            WelcomeOptionsScreen(navController = navHostController)
        }
        composable("login_screen") {
            LoginScreen(navController = navHostController)
        }
        composable("signup1_screen") {
            SignUp1Screen(navController = navHostController, userData.value)
        }
        composable("signup2_screen") {
            SignUp2Screen(navController = navHostController, userData.value)
        }
        composable("fav_screen") {
            FavScreen(
                //recipeViewModel = recipeViewModel,
                userViewModel = userViewModel,
                recipeViewModel = recipeViewModel,
                navController = navHostController
            )
        }
        composable("profile_screen") {
            ProfileScreen(navController = navHostController)
        }
        composable("recipeform1_screen") {
            RecipeForm1Screen(navController = navHostController, recipeViewModel = recipeViewModel)
        }
        composable(
            "recipeform2_screen/{title}/{description}/{imageUri}/{ingredients}",
            arguments = listOf(
                navArgument("title") { type = NavType.StringType },
                navArgument("description") { type = NavType.StringType },
                navArgument("imageUri") { type = NavType.StringType },
                navArgument("ingredients") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val title = Uri.decode(backStackEntry.arguments?.getString("title") ?: "")
            val description = Uri.decode(backStackEntry.arguments?.getString("description") ?: "")
            val imageUri = backStackEntry.arguments?.getString("imageUri")?.let { Uri.parse(Uri.decode(it)) }
            val ingredientsString = Uri.decode(backStackEntry.arguments?.getString("ingredients") ?: "")
            val ingredients = ingredientsString.split(";")

            RecipeForm2Screen(
                navController = navHostController,
                title = title,
                description = description,
                imageUri = imageUri,
                ingredients = ingredients
            )
        }
        composable(route = "IngredientRow") {
            IngredientListScreen(viewModel = ingredientViewModel)
        }
        composable(route = "RecipeList") {
            PopularRecipesScreen(
                recipeViewModel = recipeViewModel,
                ingredientViewModel = ingredientViewModel,
                recipeFilterViewModel = recipeFilterViewModel,
                userViewModel = userViewModel,
                navController = navHostController
            )
        }
        composable(route = "RecipeDetail/{recipeTitle}") { backStackEntry ->
            val recipeTitle = backStackEntry.arguments?.getString("recipeTitle") ?: return@composable
            val recipeState = remember { mutableStateOf<Recipe?>(null) }
            val isLoading = remember { mutableStateOf(true) }

            LaunchedEffect(recipeTitle) {
                val recipe = recipeViewModel.loadRecipeByTitleFromRepository(recipeTitle)
                recipeState.value = recipe
                isLoading.value = false
            }

            if (isLoading.value) {
                CircularProgressIndicator(modifier = Modifier.fillMaxSize())
            } else if (recipeState.value != null) {
                RecipeDetailScreen(recipe = recipeState.value!!, onBack = { navHostController.popBackStack() })
            } else {
                Text("Recipe not found", modifier = Modifier.fillMaxSize())
            }
        }
    }
}