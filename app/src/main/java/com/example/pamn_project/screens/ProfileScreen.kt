package com.example.pamn_project.screens

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.pamn_project.services.AuthService
import com.example.pamn_project.services.FirebaseDatabaseService
import android.util.Base64
import android.util.Base64.DEFAULT

@Composable
fun ProfileScreen(navController: NavController) {
    val currentUser = AuthService.getCurrentUser()
    val context = LocalContext.current
    var profileImageBitmap by remember { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(currentUser) {
        currentUser?.uid?.let { userId ->
            val base64Image = FirebaseDatabaseService.getProfileImageBase64(userId)
            base64Image?.let {
                // Decodificar la imagen de Base64 a Bitmap
                val decodedBytes = Base64.decode(it, DEFAULT)
                profileImageBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
            }
        }
    }

    // Mostrar el perfil
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.tertiary)
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        profileImageBitmap?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(120.dp)
                    .background(MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(60.dp))
                    .padding(4.dp)
            )
        } ?: Icon(
            imageVector = Icons.Default.AccountCircle,
            contentDescription = "Default Profile Picture",
            modifier = Modifier.size(120.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Mostrar el nombre y email
        val displayName = currentUser?.displayName ?: "Default User"
        val email = currentUser?.email ?: "No email available"

        Text("Name: $displayName", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Email: $email", style = MaterialTheme.typography.bodyMedium)
    }
}