package com.pamn.letscook.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

val shapes = Shapes(
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(8.dp),
    large = RoundedCornerShape(16.dp)
)
// Define colores personalizados para Light y Dark Theme
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFF0E8BD), // Blanco en modo oscuro
    onPrimary = Color(0xFF090801), // Negro en modo oscuro
    primaryContainer = Color(0xFFDDBB13), // Amarillo medio
    secondary = Color(0xFF4A790B), // Verde oscurito
    onSecondary = Color(0xFF4CBC10), // Verde cantoso
    background = Color(0xFF433B0F), // Amarillo muy oscuro, marrón
    surface = Color(0xFF433B0F), // Igual que el fondo para coherencia
    onBackground = Color(0xFFF0E8BD), // Blanco como color principal en fondo oscuro
    onSurface = Color(0xFFF0E8BD)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF0E0A01), // Negro
    onPrimary = Color(0xFFFEFDF6), // Blanco
    primaryContainer = Color(0xFFECCA22), // Amarillo cantoso
    secondary = Color(0xFFC4F486), // Verde pastel
    onSecondary = Color(0xFF7FEF43), // Verde cantoso
    background = Color(0xFFF0E8BD), // Amarillo pastel
    surface = Color(0xFFF0E8BD), // Igual que el fondo para coherencia
    onBackground = Color(0xFF0E0A01), // Negro en modo claro
    onSurface = Color(0xFF0E0A01)
)

@Composable
fun LetsCookTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) DarkColorScheme else LightColorScheme
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = shapes,
        content = content
    )
}
