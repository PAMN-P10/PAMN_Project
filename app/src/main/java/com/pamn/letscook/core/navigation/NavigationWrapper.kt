package com.pamn.letscook.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import com.pamn.letscook.presentation.screens.InitialScreen

@Composable
fun NavigationWrapper(){
    val navHostController = rememberNavController() // El obnbjeto que gestiona la navegacion
    val initial = "initialScreen"

    NavHost(navController = navHostController, startDestination = initial){

        // Definición explícita: la ruta es una string
        // Se maneja como una tabla hash de rutas (strings) dentro del sistema de navegación de Jetpack Compose.
        //  no hay que realizar conversiones o inferencias. Las rutas como cadenas son fáciles de parsear.
        composable(route = initial){
            InitialScreen{
                navHostController.navigate()
            }
        }

        // Uso de tipo genérico: las rutas sin una clase u objeto
        // Requiere una capa adicional para mapear los tipos genéricos a cadenas o claves unicas
        // Costo adicional de procesamiento  debido a la necesidad de almacenar meta-información del tipo (erasure) y realizar conversiones dinámicas.
        /**
        composable<"initial">{
            InitialScreen{
                navHostController.navigate()
            }
        }
        */
    }

}