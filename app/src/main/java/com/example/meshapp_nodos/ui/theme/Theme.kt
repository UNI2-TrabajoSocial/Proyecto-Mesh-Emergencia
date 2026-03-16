package com.example.meshapp_nodos.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = MeshBlue,
    secondary = MeshGreen,
    tertiary = MeshYellow,
    background = MeshDark,
    surface = MeshCard,
    onBackground = Color.White,
    onSurface = Color.White,
    primaryContainer = Color(0xFF1C2638),
    onPrimaryContainer = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = MeshBlue,
    secondary = MeshGreen,
    tertiary = MeshYellow,
    background = Color.White,
    surface = Color.White,
    onBackground = MeshDark,
    onSurface = MeshDark
)

@Composable
fun MeshAppNodosTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}