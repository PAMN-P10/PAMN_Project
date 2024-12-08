package com.pamn.letscook.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp

// Screen de prueba
@Composable
fun InitialScreen() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.weight(1f))
        Text(text = "Initial screen", fontSize = 25.sp)
        Spacer(modifier = Modifier.weight(1f))
        Button(onClick = {}) {
            Text(text = "Navegar a HOME")
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}