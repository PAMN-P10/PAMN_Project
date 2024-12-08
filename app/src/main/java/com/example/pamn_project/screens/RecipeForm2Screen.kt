// File: app/src/main/java/com/example/pamn_project/screens/RecipeForm2Screen.kt

package com.example.pamn_project.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun RecipeForm2Screen(navController: NavHostController) {
    val scrollState = rememberScrollState()
    var instruction by remember { mutableStateOf("") }
    val steps = remember { mutableStateListOf<Pair<String, String>>() }
    var showTimerInput by remember { mutableStateOf(false) }
    var timerValue by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
    ) {
        // Input para instrucciones
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .border(1.dp, Color.Black, MaterialTheme.shapes.medium)
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colorScheme.onPrimary)
                .verticalScroll(scrollState)
        ) {
            BasicTextField(
                value = instruction,
                onValueChange = { input -> instruction = input },
                modifier = Modifier.fillMaxWidth(),
                textStyle = androidx.compose.ui.text.TextStyle(fontSize = 16.sp),
                decorationBox = { innerTextField ->
                    if (instruction.isEmpty()) {
                        Text("Add instructions step by step...", fontSize = 16.sp, color = Color.Gray)
                    }
                    innerTextField()
                }
            )
        }

        // Mostrar el temporizador
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(0.dp) // Eliminar padding en el Row
        ) {
            // IconButton con tamaño ajustado y sin padding extra
            IconButton(
                onClick = { showTimerInput = !showTimerInput },
                modifier = Modifier
                    .padding(0.dp) // Eliminar padding del IconButton
                    .size(36.dp)  // Ajustar el tamaño del IconButton (ajustar como prefieras)
                    .align(Alignment.CenterVertically) // Asegurarse que el ícono esté centrado
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Timer",
                    tint = if (showTimerInput) MaterialTheme.colorScheme.primary else Color.Gray,
                    modifier = Modifier.size(24.dp) // Cambiar tamaño del ícono (ajusta el valor como desees)
                )
            }
            // Texto al lado del ícono
            Text("Add timer for this step", style = MaterialTheme.typography.bodyMedium)
        }

        if (showTimerInput) {
            TimerInput(timerValue = timerValue, onTimerChange = { timerValue = it })
        }



        // Botón para agregar paso
        Button(
            onClick = {
                if (instruction.isNotBlank()) {
                    steps.add(Pair(instruction, if (showTimerInput) timerValue else ""))
                    instruction = ""
                    timerValue = ""
                    showTimerInput = false
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Step", color = MaterialTheme.colorScheme.primary)
        }

        Spacer(Modifier.height(8.dp))

        // Lista de pasos
        Text("Steps Added:", style = MaterialTheme.typography.bodyMedium)
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .border(1.dp, Color.Black, MaterialTheme.shapes.medium)
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colorScheme.onPrimary)
                .padding(8.dp)
        ) {
            items(steps) { step ->
                val index = steps.indexOf(step)
                InstructionRow(
                    step = step.first,
                    timer = step.second,
                    stepNumber = index + 1,
                    onRemove = { steps.removeAt(it) }
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        // Botón para enviar receta
        Button(
            onClick = { navController.navigate("my_recipes_screen") },
            modifier = Modifier
                .shadow(8.dp, shape = CircleShape)
                .fillMaxWidth()
                .defaultMinSize(minHeight = 48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Text(
                text = "Submit Recipe",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelLarge
            )
        }

        // Indicador de pasos
        Text("2/2", Modifier.align(Alignment.CenterHorizontally))
    }
}

@Composable
fun TimerInput(timerValue: String, onTimerChange: (String) -> Unit) {
    // Manejo del estado para horas, minutos y segundos.
    var hours by remember { mutableStateOf(timerValue.split(":").getOrNull(0) ?: "00") }
    var minutes by remember { mutableStateOf(timerValue.split(":").getOrNull(1) ?: "00") }
    var seconds by remember { mutableStateOf(timerValue.split(":").getOrNull(2) ?: "00") }

    // Actualizar el valor completo del temporizador al cambiar cualquier parte.
    fun updateTimer() {
        onTimerChange("${hours.padStart(2, '0')}:${minutes.padStart(2, '0')}:${seconds.padStart(2, '0')}")
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        // Campo para horas (HH)
        TimerField(
            value = hours,
            placeholder = "HH",
            onValueChange = {
                hours = it
                updateTimer()
            }
        )

        // Punto fijo entre horas y minutos
        Text(":", Modifier.padding(horizontal = 4.dp), fontSize = 20.sp)

        // Campo para minutos (MM)
        TimerField(
            value = minutes,
            placeholder = "MM",
            onValueChange = {
                minutes = it
                updateTimer()
            }
        )

        // Punto fijo entre minutos y segundos
        Text(":", Modifier.padding(horizontal = 4.dp), fontSize = 20.sp)

        // Campo para segundos (SS)
        TimerField(
            value = seconds,
            placeholder = "SS",
            onValueChange = {
                seconds = it
                updateTimer()
            }
        )
    }
}

@Composable
fun TimerField(value: String, placeholder: String, onValueChange: (String) -> Unit) {
    // Campo de texto individual con validación para 2 caracteres numéricos.
    Box(
        modifier = Modifier
            .width(48.dp) // Ancho fijo para que todos los campos tengan el mismo tamaño
            .border(1.dp, Color.Black, MaterialTheme.shapes.medium)
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.onPrimary)
            .padding(4.dp)
    ) {
        BasicTextField(
            value = value,
            onValueChange = {
                // Permitir solo 2 caracteres numéricos.
                val filtered = it.filter { char -> char.isDigit() }.take(2)
                onValueChange(filtered)
            },
            modifier = Modifier.fillMaxWidth(),
            textStyle = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp),
            decorationBox = { innerTextField ->
                if (value.isEmpty()) {
                    Text(placeholder, color = Color.Gray, style = MaterialTheme.typography.bodyMedium)
                }
                innerTextField()
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
    }
}


@Composable
fun InstructionRow(step: String, timer: String, stepNumber: Int, onRemove: (Int) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
    ) {
        Text("$stepNumber. $step ${if (timer.isNotEmpty()) "($timer)" else ""}", Modifier.weight(1f))
        IconButton(onClick = { onRemove(stepNumber - 1) }) {
            Icon(imageVector = Icons.Default.Delete, contentDescription = "Remove Step", tint = Color.Red)
        }
    }
}