package com.example.pamn_project.screens

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Base64
import android.util.Log
import androidx.compose.foundation.Image
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.pamn_project.R
import com.example.pamn_project.services.AuthService
import com.example.pamn_project.services.RecipeService
import java.io.InputStream

@Composable
fun RecipeForm2Screen(
    navController: NavHostController,
    title: String,
    description: String,
    imageUri: Uri?,
    ingredients: List<String>
) {
    // Usamos los datos que recibimos a través de la navegación
    LaunchedEffect(Unit) {
        Log.d("RecipeForm2Screen", "Title: $title")
        Log.d("RecipeForm2Screen", "Description: $description")
        Log.d("RecipeForm2Screen", "Image URI: $imageUri")
        Log.d("RecipeForm2Screen", "Ingredients: $ingredients")
    }

    //val context = LocalContext.current // Obtener el contexto directamente
    // Convertir la imagen a Base64 si existe
    /*val base64Image = imageUri?.let { convertImageToBase64(context, it) }
    Log.d("Base64", "Imagen en Base64: $base64Image")*/
    val context = LocalContext.current
    val imageBase64 = remember(imageUri) {
        imageUri?.let { convertImageToBase64(context, it) }
    }
    Log.d("Base64", "Imagen en Base64: $imageBase64")


    val userId = AuthService.userId // Obtener UID directamente



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

        val imageUri2 = Uri.parse("$imageUri")  // Esta es solo una URI de ejemplo
        Log.d("RecipeForm2Screen", "imageUri2: $imageUri2")
        ShowImage(uri = imageUri2!!)

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
            modifier = Modifier.padding(top = 4.dp) // Eliminar padding en el Row
        ) {
            // IconButton con tamaño ajustado y sin padding extra
            IconButton(
                onClick = { showTimerInput = !showTimerInput },
                modifier = Modifier
                    .padding(0.dp) // Eliminar padding del IconButton
                    .size(25.dp)  // Ajustar el tamaño del IconButton (ajustar como prefieras)
                    .align(Alignment.CenterVertically) // Asegurarse que el ícono esté centrado
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Timer",
                    tint = if (showTimerInput) MaterialTheme.colorScheme.primary else Color.Gray,
                    modifier = Modifier.padding(0.dp).fillMaxSize() // Cambiar tamaño del ícono (ajusta el valor como desees)
                )
            }
            // Imgen timer
            Image(
                painter = painterResource(R.drawable.timer),
                contentDescription = "Timer",
                modifier = Modifier.size(36.dp)
            )
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
            onClick = {
                /*if (userId != null) {
                    val recipe = mapOf(
                        "title" to title,
                        "description" to description,
                        "image" to (imageBase64 ?: ""),
                        "ingredients" to ingredients,
                        "steps" to steps.mapIndexed { index, step ->
                            mapOf(
                                "stepNumber" to (index + 1).toString(),
                                "instruction" to step.first,
                                "timer" to step.second
                            )
                        },
                        "userId" to userId
                    )
                    RecipeService.uploadRecipe(recipe)*/
                    navController.navigate("tem_home_screen")
                /*} else {
                    Log.e("RecipeForm2Screen", "User ID is null. Cannot upload recipe.")
                }*/
            },
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
    var hours by remember { mutableStateOf("") }
    var minutes by remember { mutableStateOf("") }
    var seconds by remember { mutableStateOf("") }

    // Actualizar el valor completo del temporizador al cambiar cualquier parte.
    fun updateTimer() {
        onTimerChange(
            "${hours.padStart(2, '0')}:${minutes.padStart(2, '0')}:${seconds.padStart(2, '0')}"
        )
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
    Box(
        modifier = Modifier
            .width(48.dp)
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
                    // Mostrar placeholder si el campo está vacío
                    Text(
                        placeholder,
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp)
                    )
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

// Función para convertir la imagen a base64
fun convertImageToBase64(context: Context, imageUri: Uri): String {
    val contentResolver = context.contentResolver

// Asegúrate de que la URI tenga permisos de lectura
    val inputStream = contentResolver.openInputStream(imageUri)
    inputStream?.let {
        val byteArray = it.readBytes()
        it.close()
        val base64Image = Base64.encodeToString(byteArray, Base64.DEFAULT)
        Log.d("Base64", "Imagen en Base64: $base64Image")
    }
    return ""
}


@Composable
fun ShowImage(uri: Uri) {
    // Usamos Coil para cargar la imagen desde la URI
    val painter = rememberAsyncImagePainter(uri)

    when (val state = painter.state) {
        is AsyncImagePainter.State.Loading -> {
            Log.d("ShowImage", "Loading image...")
            Text("Loading image...")
        }
        is AsyncImagePainter.State.Success -> {
            Log.d("ShowImage", "Image loaded successfully!")
        }
        is AsyncImagePainter.State.Error -> {
            Log.e("ShowImage", "Error loading image: ${state.result.throwable}")
            Text("Error loading image")
        }
        else -> {
            // Mostrar la imagen si el estado es exitoso
            Image(painter = painter, contentDescription = "Selected Image")
        }
    }


    // Mostrar la imagen usando Image
    Image(
        painter = painter,
        contentDescription = "Selected Image"
    )
}