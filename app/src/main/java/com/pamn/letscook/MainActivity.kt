package com.pamn.letscook

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.firestore.FirebaseFirestore
// import androidx.navigation.NavHostController
// import androidx.navigation.compose.rememberNavController
import com.pamn.letscook.core.navigation.NavigationWrapper
import com.pamn.letscook.data.repositories.IngredientRepository
import com.pamn.letscook.domain.usecases.IngredientInitializer
import com.pamn.letscook.presentation.viewmodel.IngredientViewModel
import com.pamn.letscook.presentation.viewmodel.IngredientViewModelFactory
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LetsCookTheme {
                val factory = IngredientViewModelFactory(
                    ingredientRepository,
                    ingredientInitializer
                )
                val viewModel: IngredientViewModel = viewModel(factory = factory)

                // Call initializeIngredients() when the app starts
                LaunchedEffect(Unit) {
                    viewModel.initializeIngredients()
                }

                NavigationWrapper(
                    ingredientRepository,
                    ingredientInitializer
                )
            }
        }
    }
}
