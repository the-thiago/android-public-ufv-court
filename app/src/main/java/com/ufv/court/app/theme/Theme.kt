package com.ufv.court.app.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorPalette = lightColors(
    primary = DarkRed,
//    secondary = Teal200,
    secondary = Golden,
    background = Color.White,
    surface = Solitude,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = ShipCove,
    onSurface = BlackRock
)

@Composable
fun UFVCourtTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = LightColorPalette,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}