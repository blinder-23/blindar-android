package com.practice.hanbitlunch.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.practice.hanbitlunch.R

val NanumSquareRound = FontFamily(
    Font(R.font.nanum_square_round_regular, FontWeight.Normal),
    Font(R.font.nanum_square_round_light, FontWeight.Light),
    Font(R.font.nanum_square_round_bold, FontWeight.Bold),
    Font(R.font.nanum_square_round_extrabold, FontWeight.ExtraBold),
)

val Typography = Typography(
    defaultFontFamily = NanumSquareRound,
    // Big title
    h1 = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 40.sp,
    ),
    // Title
    h2 = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 36.sp,
    ),
    // Subtitle
    h3 = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
    ),
    // Body
    body1 = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
    ),
    // Date
    body2 = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
    ),
    // Button text
    button = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
    )
)