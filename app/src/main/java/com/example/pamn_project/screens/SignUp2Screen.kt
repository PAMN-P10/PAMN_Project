package com.example.pamn_project.screens

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.pamn_project.services.FirebaseDatabaseService
import com.google.firebase.auth.FirebaseAuth
import java.io.ByteArrayOutputStream
import kotlinx.coroutines.launch

@Composable
fun SignUp2Screen(navController: NavController) {
    var profileImageUri by remember { mutableStateOf<Uri?>(null) }
    var isImageSelected by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()  // Crear el coroutineScope para uso en onClick

    // Función para manejar la selección de imagen
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            profileImageUri = it
            isImageSelected = true
        }
    }

    // Función para convertir la imagen seleccionada a Base64
    fun encodeImageToBase64(context: Context, imageUri: Uri): String {
        val inputStream = context.contentResolver.openInputStream(imageUri)
        val bitmap = BitmapFactory.decodeStream(inputStream)

        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()

        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.secondary)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isImageSelected && profileImageUri != null) {
            Image(
                painter = rememberAsyncImagePainter(profileImageUri),
                contentDescription = "Profile Picture",
                modifier = Modifier.size(150.dp)
            )
        } else {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Default Profile Picture",
                modifier = Modifier
                    .size(450.dp)
                    .weight(1f),
                tint = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        IconButton(
            onClick = { launcher.launch("image/*") },
            modifier = Modifier
                .size(150.dp)
                .background(MaterialTheme.colorScheme.primaryContainer, shape = CircleShape)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Upload",
                tint = Color.Black
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text("Upload a pfp", fontSize = 14.sp, color = MaterialTheme.colorScheme.primary)

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (profileImageUri != null) {
                    val base64Image = encodeImageToBase64(context, profileImageUri!!)

                    val userId = FirebaseAuth.getInstance().currentUser?.uid
                    if (userId != null) {
                        coroutineScope.launch {
                            try {
                                FirebaseDatabaseService.saveProfileImageBase64(userId, base64Image)
                                navController.navigate("complete_signup")
                            } catch (e: Exception) {
                                Toast.makeText(context, "Error uploading profile picture", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        Toast.makeText(context, "User not authenticated", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Please select a profile picture", Toast.LENGTH_SHORT).show()
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth(0.8f)
        ) {
            Text("Sign Up", color = Color(0xFF0E0A01), fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text("2/2", fontSize = 14.sp, color = MaterialTheme.colorScheme.primary)
    }
}