package com.example.pamn_project.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.pamn_project.R

@Composable
fun WelcomeOptionsScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.7f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Button(
                    onClick = { navController.navigate("login_screen") },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    ),
                    modifier = Modifier
                        .shadow(8.dp, shape = CircleShape)
                        .clip(CircleShape)
                ) {
                    Text("Log In", color = MaterialTheme.colorScheme.primary)
                }
                Button(
                    onClick = { navController.navigate("signup1_screen") },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    ),
                    modifier = Modifier
                        .shadow(8.dp, shape = CircleShape)
                        .clip(CircleShape)
                ) {
                    Text("Sign Up", color = MaterialTheme.colorScheme.primary)
                }
            }
            Spacer(modifier = Modifier.height(50.dp))
            Button(
                onClick = { /* Sign up with Google logic */ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                modifier = Modifier
                    .shadow(8.dp, shape = CircleShape)
                    .fillMaxWidth(0.8f)
                    .defaultMinSize(minHeight = 48.dp)
            ) {
                Text(
                    text = "Sign up with Google",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
        Image(
            painter = painterResource(R.drawable.ingredients_bag),
            contentDescription = "Ingredients Bag",
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .align(Alignment.BottomCenter),
            contentScale = ContentScale.Crop,
            alignment = Alignment.TopCenter
        )
    }
}