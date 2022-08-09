package com.practice.hanbitlunch.theme

import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color
import com.practice.hanbitlunch.R

private val HanbitBlue = Color(R.color.hanbit_blue)
private val HanbitYellow = Color(R.color.hanbit_yellow)

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