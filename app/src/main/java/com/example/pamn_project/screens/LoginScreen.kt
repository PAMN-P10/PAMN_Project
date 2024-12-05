package com.example.pamn_project.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.pamn_project.R
import com.example.pamn_project.services.AuthService
import kotlinx.coroutines.launch
import java.util.regex.Pattern

@Composable
fun LoginScreen(navController: NavHostController) {
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val emailError = remember { mutableStateOf<String?>(null) }
    val passwordError = remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()

    val emailRegex = Pattern.compile("^[\\w-.]+@[\\w-]+\\.+[\\w-]{2,}$")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.secondary),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .background(MaterialTheme.colorScheme.secondary),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(contentAlignment = Alignment.TopStart) {
                Image(
                    painter = painterResource(R.drawable.abstract_circle),
                    contentDescription = "Abstract Circle",
                    modifier = Modifier.size(250.dp)
                )
                Image(
                    painter = painterResource(R.drawable.chef_hat),
                    contentDescription = "Ingredients Bag",
                    modifier = Modifier.size(250.dp)
                )
            }
            Spacer(modifier = Modifier.height(50.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                TextField(
                    value = email.value,
                    onValueChange = {
                        email.value = it
                        emailError.value = null
                    },
                    isError = emailError.value != null,
                    placeholder = { Text("Enter your email") },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.background,
                        unfocusedContainerColor = MaterialTheme.colorScheme.background,
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                        unfocusedLabelColor = MaterialTheme.colorScheme.primary,
                        cursorColor = MaterialTheme.colorScheme.primary
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Email
                    ),
                    trailingIcon = {
                        if (emailError.value != null) {
                            Icon(
                                imageVector = Icons.Filled.Warning,
                                contentDescription = "Error",
                                tint = Color.Red
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                )
                emailError.value?.let {
                    Text(
                        text = it,
                        color = Color.Red,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {

                TextField(
                    value = password.value,
                    onValueChange = {
                        password.value = it
                        passwordError.value = null
                    },
                    isError = passwordError.value != null,
                    placeholder = { Text("Enter your password") },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.background,
                        unfocusedContainerColor = MaterialTheme.colorScheme.background,
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                        unfocusedLabelColor = MaterialTheme.colorScheme.primary,
                        cursorColor = MaterialTheme.colorScheme.primary
                    ),
                    visualTransformation = PasswordVisualTransformation(),
                    trailingIcon = {
                        if (passwordError.value != null) {
                            Icon(
                                imageVector = Icons.Filled.Warning,
                                contentDescription = "Error",
                                tint = Color.Red
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                )
                passwordError.value?.let {
                    Text(
                        text = it,
                        color = Color.Red,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    coroutineScope.launch {
                        when {
                            !emailRegex.matcher(email.value).matches() -> {
                                emailError.value = "Invalid email format"
                            }

                            password.value.isEmpty() -> {
                                passwordError.value = "Password cannot be empty"
                            }

                            else -> {
                                val loginResult = AuthService.loginUser(email.value, password.value)
                                if (loginResult.isSuccess) {
                                    navController.navigate("tem_home_screen")
                                } else {
                                    passwordError.value = "Incorrect password or email"
                                }
                            }
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .defaultMinSize(minHeight = 48.dp)
                    .shadow(8.dp, shape = MaterialTheme.shapes.medium)
            ) {
                Text("Log In", color = MaterialTheme.colorScheme.primary)
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}