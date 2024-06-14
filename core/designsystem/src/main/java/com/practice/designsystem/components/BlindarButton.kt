package com.practice.designsystem.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.practice.designsystem.DarkPreview
import com.practice.designsystem.theme.BlindarTheme

@Composable
fun BlindarButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(16.dp),
    border: BorderStroke = BorderStroke(
        width = 2.dp,
        color = MaterialTheme.colorScheme.primaryContainer,
    ),
    isPrimary: Boolean = true,
    content: @Composable RowScope.() -> Unit = {},
) {
    val containerColor by animateColorAsState(
        targetValue = if (isPrimary) MaterialTheme.colorScheme.primaryContainer else Color.Transparent,
        animationSpec = tween(durationMillis = 500),
        label = "BlindarButton background",
    )

    Button(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(containerColor = containerColor),
        border = border,
        shape = shape,
    ) {
        CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onPrimaryContainer) {
            content()
        }
    }
}

@DarkPreview
@Composable
private fun BlindarButtonPreview() {
    var enabled by remember { mutableStateOf(true) }
    BlindarTheme {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
        ) {
            BlindarButton(
                onClick = { enabled = !enabled },
                isPrimary = enabled,
            ) {
                BodyLarge(text = "메모 편집하기")
            }
        }
    }
}