package com.example.pamn_project.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun TextFieldWithWarning(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    warning: String?,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    Column {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            modifier = Modifier.fillMaxWidth(),
            isError = warning != null,
            visualTransformation = visualTransformation
        )
        if (warning != null) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = warning,
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
