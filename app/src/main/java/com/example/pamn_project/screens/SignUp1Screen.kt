package com.example.pamn_project.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.pamn_project.ui.components.TextFieldWithWarning

@Composable
fun SignUp1Screen(navController: NavController, onCollectData: String.Companion) {
    var email by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var repeatPassword by remember { mutableStateOf("") }

    var emailWarning by remember { mutableStateOf<String?>(null) }
    var usernameWarning by remember { mutableStateOf<String?>(null) }
    var passwordWarning by remember { mutableStateOf<String?>(null) }
    var repeatPasswordWarning by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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
                if (emailWarning == null && usernameWarning == null && passwordWarning == null && repeatPasswordWarning == null) {
                    onCollectData(email, username, password)
                    navController.navigate("signup2_screen")
                }
            },
            modifier = Modifier.fillMaxWidth(0.8f)
        ) {
            Text("Next")
        }
    }
}
