package com.practice.hanbitlunch.theme

import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color

private val HanbitBlue = Color(0xFF0B4F82)
private val HanbitYellow = Color(0xFFFCDD4F)

val LightColorPalette = lightColors(
    primary = HanbitBlue,
    secondary = HanbitYellow,
    onPrimary = Color.White,
    onSecondary = Color.Black,
)

val DarkColorPalette = darkColors(
    primary = HanbitBlue,
    secondary = HanbitYellow,
    onPrimary = Color.White,
    onSecondary = Color.Black
)