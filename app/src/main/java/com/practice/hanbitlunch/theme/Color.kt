package com.practice.hanbitlunch.theme

import androidx.compose.material.Colors
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.practice.hanbitlunch.R

private val HanbitBlue: Color
    @Composable get() = colorResource(R.color.hanbit_blue)
private val HanbitYellow: Color
    @Composable get() = colorResource(R.color.hanbit_yellow)

val LightColorPalette: Colors
    @Composable get() = lightColors(
        primary = HanbitBlue,
        secondary = HanbitYellow,
        onPrimary = Color.White,
        onSecondary = Color.Black,
    )

val DarkColorPalette: Colors
    @Composable get() = darkColors(
        primary = HanbitBlue,
        secondary = HanbitYellow,
        onPrimary = Color.White,
        onSecondary = Color.Black
    )