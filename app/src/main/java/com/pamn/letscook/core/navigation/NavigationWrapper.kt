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
        composable(route = initial){
            InitialScreen()
        }
    }

}