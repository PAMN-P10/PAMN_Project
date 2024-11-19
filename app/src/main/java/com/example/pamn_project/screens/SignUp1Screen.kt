package com.example.pamn_project.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.pamn_project.services.AuthService
import kotlinx.coroutines.launch

@Composable
fun TextFieldWithWarning(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    warning: String? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    Column {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            isError = warning != null,
            visualTransformation = visualTransformation,
            modifier = Modifier.fillMaxWidth()
        )

        warning?.let {
            Spacer(modifier = Modifier.height(4.dp))
            Text(it, color = Color.Red, fontSize = 12.sp)
        }
    }
}


@Composable
fun SignUp1Screen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var repeatPassword by remember { mutableStateOf("") }

    // For warnings
    var emailWarning by remember { mutableStateOf<String?>(null) }
    var usernameWarning by remember { mutableStateOf<String?>(null) }
    var passwordWarning by remember { mutableStateOf<String?>(null) }
    var repeatPasswordWarning by remember { mutableStateOf<String?>(null) }
    var signUpError by remember { mutableStateOf<String?>(null) }

    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.secondary)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = rememberAsyncImagePainter("file:///android_asset/chef_hat.png"),
            contentDescription = "Chef's Hat",
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextFieldWithWarning(
            label = "Email",
            value = email,
            onValueChange = {
                email = it
                emailWarning = if (!android.util.Patterns.EMAIL_ADDRESS.matcher(it).matches()) {
                    "Invalid email format"
                } else null
            },
            warning = emailWarning
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextFieldWithWarning(
            label = "Username",
            value = username,
            onValueChange = {
                username = it
                usernameWarning = if (!it.matches(Regex("^[A-Za-z0-9_-]+$"))) {
                    "Invalid username format"
                } else null
            },
            warning = usernameWarning
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextFieldWithWarning(
            label = "Password",
            value = password,
            onValueChange = {
                password = it
                passwordWarning = when {
                    it.length < 8 -> "Minimum of 8 characters"
                    !it.any { char -> char.isUpperCase() } -> "At least 1 uppercase letter"
                    !it.any { char -> char.isDigit() } -> "At least 1 digit"
                    else -> null
                }
            },
            warning = passwordWarning,
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextFieldWithWarning(
            label = "Repeat Password",
            value = repeatPassword,
            onValueChange = {
                repeatPassword = it
                repeatPasswordWarning = if (it != password) {
                    "Passwords do not match"
                } else null
            },
            warning = repeatPasswordWarning,
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                coroutineScope.launch {
                    val result = AuthService.registerUser(email, password)
                    result.onSuccess {
                        navController.navigate("signup2_screen")
                    }.onFailure {
                        signUpError = it.message
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth(0.8f)
        ) {
            Text("Next", color = MaterialTheme.colorScheme.primary, fontSize = 16.sp)
        }

        signUpError?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(it, color = Color.Red, fontSize = 12.sp)
        }
    }
}