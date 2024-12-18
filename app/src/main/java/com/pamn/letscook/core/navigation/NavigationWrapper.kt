package com.pamn.letscook.core.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import com.google.firebase.firestore.FirebaseFirestore
import com.pamn.letscook.data.repositories.IngredientRepository
import com.pamn.letscook.data.repositories.RecipeInitializer
import com.pamn.letscook.data.repositories.RecipeRepository
import com.pamn.letscook.domain.usecases.IngredientInitializer
import com.pamn.letscook.presentation.components.IngredientListScreen
import com.pamn.letscook.presentation.screens.PopularRecipesScreen
import com.pamn.letscook.presentation.screens.InitialScreen
import com.pamn.letscook.presentation.screens.HomeScreen
import com.pamn.letscook.presentation.viewmodel.IngredientViewModel
import com.pamn.letscook.presentation.viewmodel.IngredientViewModelFactory
import com.pamn.letscook.presentation.viewmodel.RecipeViewModel
import com.pamn.letscook.presentation.viewmodel.RecipeViewModelFactory

@Composable
fun NavigationWrapper(
    // Pasados como dependencia
    ingredientRepository: IngredientRepository,
    ingredientInitializer: IngredientInitializer,
    recipeRepository: RecipeRepository,
    recipeInitializer: RecipeInitializer

){
    val navHostController = rememberNavController() // El objeto que gestiona la navegacion
    // Advice for myself: Las constantes de ruta debería de centralizarse en un archivo separado cuando
    val initial = "InitialScreen"
    val home = "HomeScreen"
    val ingredientRow = "IngredientRow"
    val recipeList = "RecipeList"

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
            // Instanciar la ViewModelFactory
            val factory = IngredientViewModelFactory(
                ingredientRepository = ingredientRepository,
                ingredientInitializer = ingredientInitializer
            )

            // Crear la instancia del ViewModel utilizando la fábrica
            val ingredientViewModel: IngredientViewModel = viewModel(factory = factory)

            HomeScreen(viewModel = ingredientViewModel, repository = IngredientRepository(
                FirebaseFirestore.getInstance())

            )
        }
        composable(route = ingredientRow) {
            // Instanciar la ViewModelFactory
            val factory = IngredientViewModelFactory(
                ingredientRepository = ingredientRepository,
                ingredientInitializer = ingredientInitializer
            )

            // Crear la instancia del ViewModel utilizando la fábrica
            val ingredientViewModel: IngredientViewModel = viewModel(factory = factory)

            IngredientListScreen(viewModel = ingredientViewModel)
        }

        composable(route = recipeList) {
            // Instanciar la ViewModelFactory
            val factory = RecipeViewModelFactory(
                recipeRepository = recipeRepository,
                recipeInitializer = recipeInitializer
            )

            // Crear la instancia del ViewModel utilizando la fábrica
            val recipeViewModel: RecipeViewModel = viewModel(factory = factory)

            PopularRecipesScreen(recipeViewModel = recipeViewModel)
        }


    }

}