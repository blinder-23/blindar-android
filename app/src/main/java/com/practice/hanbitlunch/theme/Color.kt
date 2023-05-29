package com.practice.hanbitlunch.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
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

val LightColorPalette: ColorScheme
    @Composable get() = lightColorScheme(
        primary = BlindarBlue,
        secondary = BlindarBlueVariant,
        tertiary = BlindarYellow,
        onPrimary = Color.White,
        onSecondary = Color.White,
        onTertiary = Color.Black,
    )

val DarkColorPalette: ColorScheme
    @Composable get() = darkColorScheme(
        primary = BlindarBlue,
        secondary = BlindarBlueVariant,
        tertiary = BlindarYellow,
        onPrimary = Color.White,
        onSecondary = Color.White,
        onTertiary = Color.Black,
    )