package com.example.pamn_project.screens

import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.pamn_project.R
import com.example.pamn_project.services.AuthService
import kotlinx.coroutines.launch

@Composable
fun SignUp2Screen(
    navController: NavController,
    signUpData: MutableMap<String, String>
) {
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var errorMessage by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    val imagePickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.secondary),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.9f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (selectedImageUri != null) {
                val bitmap = remember(selectedImageUri) {
                    BitmapFactory.decodeStream(context.contentResolver.openInputStream(selectedImageUri!!))
                }?.asImageBitmap()

                if (bitmap != null) {
                    Image(
                        bitmap = bitmap,
                        contentDescription = "Selected PFP",
                        modifier = Modifier
                            .size(350.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }
            } else {
                Icon(
                    painter = painterResource(R.drawable.default_pfp),
                    contentDescription = "Default PFP",
                    modifier = Modifier
                        .size(350.dp)
                        .clip(CircleShape),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { imagePickerLauncher.launch("image/*") },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Text("Upload a PFP", color = MaterialTheme.colorScheme.primary)
            }

            Spacer(modifier = Modifier.height(80.dp))
            Button(
                onClick = {
                    coroutineScope.launch {
                        try {
                            val pfpBase64 = selectedImageUri?.let { AuthService.convertImageToBase64(context, it) } ?: ""
                            val result = AuthService.registerUser(
                                signUpData["email"] ?: "",
                                signUpData["password"] ?: "",
                                signUpData["username"] ?: "",
                                pfpBase64
                            )
                            result.onSuccess {
                                navController.navigate("profile_screen")
                            }.onFailure {
                                errorMessage = "Error registering user: ${it.localizedMessage}"
                                Toast.makeText(context, "Error: ${it.localizedMessage}", Toast.LENGTH_LONG).show()
                            }
                        } catch (e: Exception) {
                            errorMessage = "Unexpected error: ${e.localizedMessage}"
                            Toast.makeText(context, "Unexpected error: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                modifier = Modifier.fillMaxWidth(0.6f)
            ) {
                Text(
                    text = "Sign Up",
                    color = MaterialTheme.colorScheme.primary
                )
            }

            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text("2/2", style = MaterialTheme.typography.labelLarge)
        }
    }
}