package com.practice.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import com.practice.designsystem.R

private val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

private val popupFontName = GoogleFont("Poppins")

private val popupFontFamily = FontFamily(
    Font(
        googleFont = popupFontName,
        fontProvider = provider,
        weight = FontWeight.SemiBold,
    ),
    Font(
        googleFont = popupFontName,
        fontProvider = provider,
        weight = FontWeight.Medium,
    )
)

val PopupTypography = Typography(
    titleLarge = TextStyle(
        fontFamily = popupFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 26.sp,
    ),
    titleMedium = TextStyle(
        fontFamily = popupFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
    ),
    bodyMedium = TextStyle(
        fontFamily = popupFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp,
    ),
    bodySmall = TextStyle(
        fontFamily = popupFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
    )
)