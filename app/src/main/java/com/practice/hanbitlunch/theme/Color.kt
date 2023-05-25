package com.practice.hanbitlunch.theme

import androidx.compose.material.Colors
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.practice.hanbitlunch.R

private val BlindarBlue: Color
    @Composable get() = colorResource(R.color.blindar_blue)
private val BlindarBlueVariant: Color
    @Composable get() = colorResource(R.color.blindar_blue_variant)
private val BlindarYellow: Color
    @Composable get() = colorResource(R.color.blindar_yellow)

val LightColorPalette: Colors
    @Composable get() = lightColors(
        primary = BlindarBlue,
        primaryVariant = BlindarBlueVariant,
        secondary = BlindarYellow,
        onPrimary = Color.White,
        onSecondary = Color.Black,
    )

val DarkColorPalette: Colors
    @Composable get() = darkColors(
        primary = BlindarBlue,
        primaryVariant = BlindarBlueVariant,
        secondary = BlindarYellow,
        onPrimary = Color.White,
        onSecondary = Color.Black
    )