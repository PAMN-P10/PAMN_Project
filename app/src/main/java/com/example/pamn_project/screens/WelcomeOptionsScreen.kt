package com.example.pamn_project.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter

@Composable
fun WelcomeOptionsScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = { navController.navigate("login_screen") },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC4F486)),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .padding(8.dp)
                    .shadow(8.dp, shape = RoundedCornerShape(8.dp))
            ) {
                Text(text = "Log In", color = Color(0xFF0E0A01), fontSize = 16.sp)
            }

            Button(
                onClick = { navController.navigate("signup1_screen") },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC4F486)),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .padding(8.dp)
                    .shadow(8.dp, shape = RoundedCornerShape(8.dp))
            ) {
                Text(text = "Sign Up", color = Color(0xFF0E0A01), fontSize = 16.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.navigate("") },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFECCA22)),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .padding(16.dp)
                .shadow(8.dp, shape = RoundedCornerShape(8.dp))
                .fillMaxWidth(0.8f)
        ) {
            Text(text = "Sign up with Google", color = Color(0xFF0E0A01), fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = rememberAsyncImagePainter("file:///android_asset/ingredients_bag.png"),
            contentDescription = "Ingredients Bag Zoomed",
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
        )
    }
}