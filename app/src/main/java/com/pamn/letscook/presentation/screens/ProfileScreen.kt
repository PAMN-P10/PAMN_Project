package com.pamn.letscook.presentation.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.pamn_project.services.AuthService
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(navController: NavHostController) {
    val coroutineScope = rememberCoroutineScope()
    val currentUser = AuthService.getCurrentUser()
    val userData = remember { mutableStateOf<Map<String, String>?>(null) }
    val originalPhoto = remember { mutableStateOf<String?>(null) }
    val editablePhoto = remember { mutableStateOf<String?>(null) }
    val isEditing = remember { mutableStateOf(false) }
    val showConfirmationDialog = remember { mutableStateOf(false) }

    // Cargar datos del usuario al inicio
    LaunchedEffect(currentUser) {
        if (currentUser != null) {
            val userInfo = AuthService.getUserData(currentUser.uid)
            userData.value = userInfo
            originalPhoto.value = userInfo?.get("pfp")
            editablePhoto.value = userInfo?.get("pfp")
        }
    }

    // Lanzador para seleccionar una imagen de la galería
    val context = LocalContext.current
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val base64Image = AuthService.convertImageToBase64(context, it)
            editablePhoto.value = base64Image
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.secondary)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "My Profile",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Box(
            modifier = Modifier
                .size(350.dp)
                .clip(CircleShape)
                .background(Color.Gray)
                .clickable(enabled = isEditing.value) {
                    if (isEditing.value) {
                        imagePickerLauncher.launch("image/*")
                    }
                }
        ) {
            val bitmap = editablePhoto.value?.let { base64 ->
                AuthService.decodeBase64ToBitmap(base64)
            }
            if (bitmap != null) {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "Profile Picture",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Default Profile Picture",
                    tint = Color.White,
                    modifier = Modifier.align(Alignment.Center)
                        .size(350.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Campos editables o inmutables
        if (userData.value != null) {
            val email = remember { mutableStateOf(userData.value!!["email"] ?: "") }
            val username = remember { mutableStateOf(userData.value!!["username"] ?: "") }

            OutlinedTextField(
                value = email.value,
                onValueChange = { email.value = it },
                label = { Text("Email") },
                enabled = isEditing.value,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.background,
                    unfocusedContainerColor = MaterialTheme.colorScheme.background,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.primary,
                    cursorColor = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = username.value,
                onValueChange = { username.value = it },
                label = { Text("Username") },
                enabled = isEditing.value,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.background,
                    unfocusedContainerColor = MaterialTheme.colorScheme.background,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.primary,
                    cursorColor = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botones Edit, Save y Cancel
            if (!isEditing.value) {
                Button(
                    onClick = { isEditing.value = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    modifier = Modifier
                        .shadow(8.dp, shape = CircleShape)
                        .fillMaxWidth(0.8f)
                        .defaultMinSize(minHeight = 48.dp)
                ) {
                    Text("Edit", color = MaterialTheme.colorScheme.primary)
                }
            } else {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Button(
                        onClick = {
                            // Cancelar edición
                            email.value = userData.value!!["email"] ?: ""
                            username.value = userData.value!!["username"] ?: ""
                            editablePhoto.value = originalPhoto.value
                            isEditing.value = false
                        },
                        colors = ButtonDefaults.buttonColors(Color.Red)
                    ) {
                        Text("Cancel", color = MaterialTheme.colorScheme.primary)
                    }

                    Button(
                        onClick = {
                            showConfirmationDialog.value = true // Mostrar el pop-up
                        },
                        colors = ButtonDefaults.buttonColors(Color.Green)
                    ) {
                        Text("Save", color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
            // Pop-up de confirmación
            if (showConfirmationDialog.value) {
                AlertDialog(
                    onDismissRequest = { showConfirmationDialog.value = false },
                    title = { Text("Confirmation") },
                    text = { Text("Are you sure you want to save these changes?") },
                    confirmButton = {
                        Button(
                            onClick = {
                                coroutineScope.launch {
                                    val updatedData = mapOf(
                                        "email" to email.value,
                                        "username" to username.value,
                                        "pfp" to (editablePhoto.value ?: "")
                                    )
                                    val result = AuthService.updateUserData(currentUser!!.uid, updatedData)
                                    if (result.isSuccess) {
                                        userData.value = updatedData
                                        originalPhoto.value = editablePhoto.value
                                        isEditing.value = false
                                    }
                                    showConfirmationDialog.value = false
                                }
                            },
                            colors = ButtonDefaults.buttonColors(Color.Green)
                        ) {
                            Text("Yes", color = MaterialTheme.colorScheme.primary)
                        }
                    },
                    dismissButton = {
                        Button(
                            onClick = { showConfirmationDialog.value = false },
                            colors = ButtonDefaults.buttonColors(Color.Red)
                        ) {
                            Text("No", color = MaterialTheme.colorScheme.primary)
                        }
                    }
                )
            }
        }
    }
}
