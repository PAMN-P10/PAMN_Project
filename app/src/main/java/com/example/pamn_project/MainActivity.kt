package com.example.pamn_project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pamn_project.screens.LoginScreen
import com.example.pamn_project.screens.ProfileScreen
import com.example.pamn_project.screens.SignUp1Screen
import com.example.pamn_project.screens.SignUp2Screen
import com.example.pamn_project.screens.WelcomeScreen
import com.example.pamn_project.screens.WelcomeOptionsScreen
import com.example.pamn_project.ui.theme.PAMN_ProjectTheme
import com.google.firebase.FirebaseApp

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
                    SignUp1Screen(navController = navController)
                }
                composable("signup2_screen") {
                    SignUp2Screen(navController = navController)
                }
                composable("profile_screen") {
                    ProfileScreen(navController = navController)
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