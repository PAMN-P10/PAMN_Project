// File: app/src/main/java/com/example/pamn_project/screens/RecipeForm2Screen.kt

package com.example.pamn_project.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.pamn_project.services.AuthService
import com.example.pamn_project.services.RecipeService
import com.example.pamn_project.viewmodels.RecipeViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeForm2Screen(navController: NavHostController) {
    val instruction = remember { mutableStateOf("") }
    val steps = remember { mutableStateListOf<String>() }
    val cookingTime = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF8E1)) // Pastel yellow
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Instruction input
        TextField(
            value = instruction.value,
            onValueChange = { instruction.value = it },
            label = { Text("Describe the instruction") },
            modifier = Modifier.fillMaxWidth()
        )

        // Add step button
        Button(
            onClick = {
                if (instruction.value.isNotBlank()) {
                    steps.add(instruction.value)
                    instruction.value = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Step")
        }

        // List of steps
        Text("Steps Added:", style = MaterialTheme.typography.bodyMedium)
        steps.forEachIndexed { index, step ->
            Text("${index + 1}. $step", Modifier.padding(8.dp))
        }

        Spacer(Modifier.height(8.dp))

        // Cooking time input
        TextField(
            value = cookingTime.value,
            onValueChange = { cookingTime.value = it },
            label = { Text("Cooking time (hh:mm:ss)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        // Submit recipe button
        Button(
            onClick = { navController.popBackStack() /* Dummy action */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Submit Recipe")
        }

        // Step indicator
        Text("Step 2/2", Modifier.align(Alignment.CenterHorizontally))
    }
}

@Composable
fun InstructionRow(
    step: String,
    stepNumber: Int,
    onRemove: (Int) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = "$stepNumber. $step",
            modifier = Modifier.weight(1f)
        )
        IconButton(onClick = { onRemove(stepNumber - 1) }) {
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = "Remove Step",
                tint = Color.Red
            )
        }
    }
}
