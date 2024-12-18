package com.pamn.letscook

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
// import androidx.navigation.NavHostController
// import androidx.navigation.compose.rememberNavController
import com.pamn.letscook.core.navigation.NavigationWrapper
import com.pamn.letscook.data.repositories.IngredientRepository
import com.pamn.letscook.data.repositories.RecipeInitializer
import com.pamn.letscook.data.repositories.RecipeRepository
import com.pamn.letscook.data.repositories.UserRepository
import com.pamn.letscook.domain.usecases.IngredientInitializer
import com.pamn.letscook.presentation.viewmodel.IngredientViewModel
import com.pamn.letscook.presentation.viewmodel.IngredientViewModelFactory
import com.pamn.letscook.presentation.viewmodel.RecipeViewModel
import com.pamn.letscook.presentation.viewmodel.RecipeViewModelFactory
import com.pamn.letscook.ui.theme.LetsCookTheme

class MainActivity : ComponentActivity() {

    //private lateinit var navHostController: NavHostController
    /**
     * Instanciar dependencias porque:
     * FirebaseFirestore no tiene un companion object
     * que permita usarlo como referencia directa
     * (FirebaseFirestore no es un singleton en sí mismo).
     */
    private val ingredientRepository by lazy {
        IngredientRepository(firestore = FirebaseFirestore.getInstance())
    }
    private val ingredientInitializer by lazy {
        IngredientInitializer(ingredientRepository)
    }

    private val userRepository by lazy {
        UserRepository(
            firebaseAuth = FirebaseAuth.getInstance(),
            firestore = FirebaseFirestore.getInstance()
        )
    }

    // Instancia de dependencias necesarias para las recetas
    private val recipeRepository by lazy {
        RecipeRepository(
            firestore = FirebaseFirestore.getInstance(),
            userRepository = userRepository,
            ingredientRepository = ingredientRepository
        )
    }

    private val recipeInitializer by lazy {
        RecipeInitializer(
            repository = recipeRepository,
            userRepository = userRepository,
            ingredientRepository = ingredientRepository
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LetsCookTheme {
                val ingredientFactory  = IngredientViewModelFactory(
                    ingredientRepository,
                    ingredientInitializer
                )
                val ingredientViewModel: IngredientViewModel = viewModel(factory = ingredientFactory )

                val recipeFactory  = RecipeViewModelFactory(
                    recipeRepository = recipeRepository,
                    recipeInitializer = recipeInitializer
                )

                // Crear la instancia del ViewModel utilizando la fábrica
                val recipeViewModel: RecipeViewModel = viewModel(factory = recipeFactory )

                // Call initializeIngredients() when the app starts
                // Llamadas para inicializar ingredientes y recetas al iniciar la app
                LaunchedEffect(Unit) {
                    ingredientViewModel.initializeIngredients()
                    recipeViewModel.initializeRecipes()
                }

                NavigationWrapper(
                    ingredientRepository = ingredientRepository,
                    ingredientInitializer = ingredientInitializer,
                    recipeRepository = recipeRepository,
                    recipeInitializer = recipeInitializer
                )
            }
        }
    }
}
