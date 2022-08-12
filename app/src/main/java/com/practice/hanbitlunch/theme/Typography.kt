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
    // Common/big-title
    h1 = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 40.sp,
    ),
    // Common/title
    h2 = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 36.sp,
    ),
    // Common/subtitle
    h3 = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
    ),
    // Common/body
    body1 = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
    ),
    // Calendar/date
    body2 = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
    ),
    // Calendar/day
    caption = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    // Button (positive, negative)
    button = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
    )
)