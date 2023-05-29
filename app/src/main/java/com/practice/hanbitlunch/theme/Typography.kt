package com.practice.hanbitlunch.theme

import androidx.compose.material3.Typography
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

/** M3 Typography doesn't have `defaultFontFamily` argument.
 * We need to use the `fontFamily` parameter in each of the individual case.
 */
val Typography = Typography(
    // Common/big-title
    displayLarge = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 40.sp,
    ),
    // Common/title
    displayMedium = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 36.sp,
    ),
    // Common/subtitle
    displaySmall = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
    ),
    // Common/body
    bodyLarge = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
    ),
    // Calendar/date
    bodyMedium = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
    ),
    // Calendar/day
    bodySmall = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    // Button (positive, negative)
    labelLarge = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
    )
)