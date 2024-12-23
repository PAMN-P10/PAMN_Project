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
import com.google.firebase.firestore.FirebaseFirestore
import com.pamn.letscook.data.repositories.IngredientRepository
import com.pamn.letscook.data.repositories.RecipeInitializer
import com.pamn.letscook.data.repositories.RecipeRepository
import com.pamn.letscook.domain.models.Recipe
import com.pamn.letscook.domain.usecases.IngredientInitializer
import com.pamn.letscook.presentation.components.IngredientListScreen
import com.pamn.letscook.presentation.screens.RecipeDetailScreen
import com.pamn.letscook.presentation.screens.PopularRecipesScreen
import com.pamn.letscook.presentation.screens.InitialScreen
import com.pamn.letscook.presentation.screens.HomeScreen
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
import com.pamn.letscook.presentation.viewmodel.RecipeFilterViewModelFactory
import com.pamn.letscook.presentation.viewmodel.UserViewModel

@Composable
fun NavigationWrapper(
    ingredientRepository: IngredientRepository,
    ingredientInitializer: IngredientInitializer,
    recipeRepository: RecipeRepository,
    recipeInitializer: RecipeInitializer
) {
    val navHostController = rememberNavController()

    // Instanciar la ViewModelFactory
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

    // Crear la instancia del UserViewModel
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
                userViewModel = userViewModel, // Asegúrate de agregar esto en la pantalla
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

        composable(route = "InitialScreen") {
            InitialScreen(
                navigateToHome = { navHostController.navigate(route = "recipeList") }
            )
        }
        composable(route = "HomeScreen") {
            HomeScreen(
                viewModel = ingredientViewModel,
                repository = IngredientRepository(FirebaseFirestore.getInstance())
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

    /**
    NavHost(navController = navHostController, startDestination = initial){

        // Definición explícita: la ruta es una string
        // Se maneja como una tabla hash de rutas (strings) dentro del sistema de navegación de Jetpack Compose.
        //  no hay que realizar conversiones o inferencias. Las rutas como cadenas son fáciles de parsear.
        composable(route = initial){
            InitialScreen(
                navigateToHome = { navHostController.navigate(route = recipeList)}
            )
        }

        // Uso de tipo genérico: las rutas sin una clase u objeto
        // Requiere una capa adicional para mapear los tipos genéricos a cadenas o claves unicas
        // Costo adicional de procesamiento  debido a la necesidad de almacenar meta-información del tipo (erasure) y realizar conversiones dinámicas.
        /**
         composable<initial>{
            InitialScreen{
                navHostController.navigate(Home)
            }
        }
        */

        composable(route = home) {
            HomeScreen(
                viewModel = ingredientViewModel,
                repository = IngredientRepository(
                FirebaseFirestore.getInstance())

            )
        }
        composable(route = ingredientRow) {

            IngredientListScreen(viewModel = ingredientViewModel)
        }

        composable(route = recipeList) {

            PopularRecipesScreen(
                recipeViewModel = recipeViewModel,
                ingredientViewModel = ingredientViewModel,
                recipeFilterViewModel = recipeFilterViewModel,
                navController = navHostController
            )
        }

        /**
        composable("RecipeDetail/{recipeTitle}") { backStackEntry ->
            val recipeTitle = backStackEntry.arguments?.getString("recipeTitle") ?: return@composable
            val recipe = recipeViewModel.recipes.value.find { it.title == recipeTitle }
            if (recipe != null) {
                RecipeDetailScreen(recipe = recipe, onBack = { navHostController.popBackStack() })
            }
        }

        composable(route = "RecipeDetail/{recipeTitle}") { backStackEntry ->
            val recipeTitle = backStackEntry.arguments?.getString("recipeTitle") ?: return@composable
            val recipe = recipeViewModel.loadRecipeByTitle(recipeTitle)
            recipe?.let {
                RecipeDetailScreen(recipe = it, onBack = { navHostController.popBackStack() })
            }
        }
         */

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
    */