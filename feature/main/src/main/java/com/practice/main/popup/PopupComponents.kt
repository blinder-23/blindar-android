package com.practice.main.popup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.practice.designsystem.LightAndDarkPreview
import com.practice.designsystem.theme.BlindarTheme
import com.practice.designsystem.theme.PopupTypography

val popupPadding = 24.dp

@Composable
fun PopupTitleLarge(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
) {
    PopupText(
        text = text,
        textStyle = PopupTypography.titleLarge,
        modifier = modifier,
        color = color,
    )
}

@Composable
fun PopupBodyMedium(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
) {
    PopupText(
        text = text,
        textStyle = PopupTypography.bodyMedium,
        modifier = modifier,
        color = color,
    )
}

@Composable
fun PopupBodySmall(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
) {
    PopupText(
        text = text,
        textStyle = PopupTypography.bodySmall,
        modifier = modifier,
        color = color,
    )
}

@Composable
private fun PopupText(
    text: String,
    textStyle: TextStyle,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
) {
    CompositionLocalProvider(LocalTextStyle provides textStyle) {
        Text(
            text = text,
            modifier = modifier,
            color = color,
        )
    }
}

@LightAndDarkPreview
@Composable
private fun PopupTitleLargePreview() {
    BlindarTheme {
        PopupTitleLarge(
            text = "영양 정보",
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp),
            color = contentColorFor(backgroundColor = MaterialTheme.colorScheme.surface)
        )
    }
}