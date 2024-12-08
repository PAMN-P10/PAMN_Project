package com.pamn.letscook

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
// import androidx.navigation.NavHostController
// import androidx.navigation.compose.rememberNavController
import com.pamn.letscook.core.navigation.NavigationWrapper
import com.pamn.letscook.ui.theme.LetsCookTheme

class MainActivity : ComponentActivity() {

    //private lateinit var navHostController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LetsCookTheme {
                NavigationWrapper()
            }
        }
    }
}
