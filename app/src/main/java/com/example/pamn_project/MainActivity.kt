package com.example.pamn_project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.pamn_project.screens.*
import com.example.pamn_project.ui.theme.PAMN_ProjectTheme
import com.example.pamn_project.services.AuthService
import com.example.pamn_project.viewmodel.RecipeViewModel
import com.google.firebase.FirebaseApp


//Código para ir directo a la pantalla deseada para ahcer puerbas del diseño
/*class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PAMN_ProjectTheme {
                Direct()
            }
        }
    }
}

@Composable
fun Direct() {
    val navController = rememberNavController()
    val recipeViewModel: RecipeViewModel = viewModel()

    // Configuramos el NavHost para una navegación directa a RecipeForm1
    NavHost(
        navController = navController,
        startDestination = "recipeform1_screen"
    ) {
        composable("recipeform1_screen") {
            RecipeForm1Screen(navController, recipeViewModel)
        }
        composable(
            "recipe_form_2_screen/{title}/{description}/{image}",
            arguments = listOf(
                navArgument("title") { type = NavType.StringType },
                navArgument("description") { type = NavType.StringType },
                navArgument("image") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val title = backStackEntry.arguments?.getString("title") ?: ""
            val description = backStackEntry.arguments?.getString("description") ?: ""
            val image = backStackEntry.arguments?.getString("image") ?: ""
            RecipeForm2Screen(navController, title, description, image)
        }

    }
}*/



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this) // Inicializa Firebase
        enableEdgeToEdge()
        setContent {
            PAMN_ProjectTheme {
                val navController = rememberNavController() // Controlador de navegación
                AppScaffold(navController = navController)
            }
        }
    }
}

@Composable
fun AppScaffold(navController: NavHostController) {
    // Variables para almacenar datos de usuario
    val userData = remember { mutableStateOf(mutableMapOf<String, String>()) }
    val coroutineScope = rememberCoroutineScope()

    // Instanciar el RecipeViewModel
    val recipeViewModel: RecipeViewModel = viewModel()

    // Cargar los datos del usuario cuando se inicie la pantalla
    LaunchedEffect(Unit) {
        val currentUser = AuthService.getCurrentUser()
        if (currentUser != null) {
            // Cargar los datos del usuario desde Firebase
            val userInfo = AuthService.getUserData(currentUser.uid)
            userData.value = userInfo?.toMutableMap() ?: mutableMapOf()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Surface(
            modifier = Modifier.padding(innerPadding)
        ) {
            // Configuración de la navegación
            NavHost(
                navController = navController,
                startDestination = "welcome_screen"
            ) {
                composable("welcome_screen") {
                    WelcomeScreen(navController = navController)
                }
                composable("welcome_options_screen") {
                    WelcomeOptionsScreen(navController = navController)
                }
                composable("login_screen") {
                    LoginScreen(navController = navController)
                }
                composable("signup1_screen") {
                    SignUp1Screen(navController, userData.value)
                }
                composable("signup2_screen") {
                    SignUp2Screen(navController, userData.value)
                }
                composable("tem_home_screen") {
                    TemHomeScreen(navController = navController)
                }
                composable("profile_screen") {
                    ProfileScreen(navController = navController)
                }
                composable("recipeform1_screen") {
                    RecipeForm1Screen(navController, recipeViewModel)
                }
                composable(
                    "recipe_form_2_screen/{title}/{description}/{image}/{ingredients}",
                    arguments = listOf(
                        navArgument("title") { type = NavType.StringType },
                        navArgument("description") { type = NavType.StringType },
                        navArgument("image") { type = NavType.StringType },
                        navArgument("ingredients") { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    val title = backStackEntry.arguments?.getString("title") ?: ""
                    val description = backStackEntry.arguments?.getString("description") ?: ""
                    val image = backStackEntry.arguments?.getString("image") ?: ""
                    val ingredientsString = backStackEntry.arguments?.getString("ingredients") ?: ""
                    val ingredients = ingredientsString.split(",").filter { it.isNotEmpty() }

                    RecipeForm2Screen(navController, title, description, image, ingredients)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PAMN_ProjectTheme {
        val navController = rememberNavController()
        AppScaffold(navController = navController)
    }
}