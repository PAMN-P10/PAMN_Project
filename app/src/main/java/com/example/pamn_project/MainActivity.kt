// File: app/src/main/java/com/example/pamn_project/MainActivity.kt

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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pamn_project.screens.*
import com.example.pamn_project.ui.theme.PAMN_ProjectTheme
import com.google.firebase.FirebaseApp
import com.example.pamn_project.services.AuthService
import com.example.pamn_project.viewmodels.RecipeViewModel

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
                    RecipeForm1Screen(navController)
                }
                composable("recipeform2_screen") {
                    RecipeForm2Screen(navController)
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