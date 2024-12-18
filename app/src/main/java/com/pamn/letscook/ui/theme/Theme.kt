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

// Light theme colors
val black = Color(0xFF0E0A01)
val white = Color(0xFFFEFDF6)
val orange = Color(0xFFECCA22)
val lightGreen = Color(0xFFC4F486)
val darkGreen = Color(0xFF7FEF43)
val beige = Color(0xFFF0E8BD)

// Dark theme colors
val blackDarkMode = Color(0xFFFEFAF1)
val whiteDarkMode = Color(0xFF090801)
val orangeDarkMode = Color(0xFFDDBB13)
val lightGreenDarkMode = Color(0xFF4A790B)
val darkGreenDarkMode = Color(0xFF4CBC10)
val beigeDarkMode = Color(0xFF433B0F)

private val DarkColorScheme = darkColorScheme(
    primary = orangeDarkMode,
    secondary = darkGreenDarkMode,
    //accent = lightGreenDarkMode,
    onPrimary = whiteDarkMode,
    surface = beigeDarkMode,
    background = blackDarkMode
)

private val LightColorScheme = lightColorScheme(
    primary = orange,
    secondary = darkGreen,
    //accent = lightGreen,
    onPrimary = white,
    surface = beige,
    background = black
)

val shapes = Shapes(
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(8.dp),
    large = RoundedCornerShape(16.dp)
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