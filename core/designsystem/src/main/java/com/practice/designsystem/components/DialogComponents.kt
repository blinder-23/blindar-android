package com.practice.designsystem.components

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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import com.practice.designsystem.LightAndDarkPreview
import com.practice.designsystem.theme.BlindarTheme
import com.practice.designsystem.theme.DialogTypography

val dialogContentPadding = 24.dp

@Composable
fun DialogTitleLarge(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
) {
    DialogText(
        text = text,
        textStyle = DialogTypography.titleLarge,
        modifier = modifier,
        color = color,
    )
}

@Composable
fun DialogBodyLarge(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
) {
    DialogText(
        text = text,
        textStyle = DialogTypography.bodyLarge,
        modifier = modifier,
        color = color,
    )
}

@Composable
fun DialogBodyMedium(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
) {
    DialogText(
        text = text,
        textStyle = DialogTypography.bodyMedium,
        modifier = modifier,
        color = color,
    )
}

@Composable
fun DialogBodySmall(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontStyle: FontStyle? = null,
) {
    DialogText(
        text = text,
        textStyle = DialogTypography.bodySmall,
        modifier = modifier,
        color = color,
        fontStyle = fontStyle,
    )
}

@Composable
private fun DialogText(
    text: String,
    textStyle: TextStyle,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontStyle: FontStyle? = null,
) {
    CompositionLocalProvider(LocalTextStyle provides textStyle) {
        Text(
            text = text,
            modifier = modifier,
            color = color,
            fontStyle = fontStyle,
        )
    }
}

@LightAndDarkPreview
@Composable
private fun DialogTitleLargePreview() {
    BlindarTheme {
        DialogTitleLarge(
            text = "영양 정보",
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp),
            color = contentColorFor(backgroundColor = MaterialTheme.colorScheme.surface)
        )
    }
}