package com.practice.designsystem.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.practice.designsystem.DarkPreview
import com.practice.designsystem.theme.BlindarTheme

@Composable
fun BlindarChip(
    text: String,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    contentColor: Color = contentColorFor(backgroundColor = containerColor),
) {
    val alpha by animateFloatAsState(
        targetValue = if (enabled) 1f else 0.7f,
        label = "BlindarChip alpha",
    )
    AssistChip(
        onClick = onClick,
        label = {
            LabelMedium(
                text = text,
                color = contentColor,
                modifier = Modifier.graphicsLayer {
                    this.alpha = alpha
                },
            )
        },
        enabled = enabled,
        modifier = modifier.graphicsLayer {
            this.alpha = alpha
        },
        colors = AssistChipDefaults.assistChipColors(
            containerColor = containerColor,
            disabledContainerColor = containerColor,
        ),
        border = BorderStroke(width = 1.dp, color = containerColor),
    )
}

@DarkPreview
@Composable
private fun PhoneNumberAuthChipPreview() {
    var enabled by remember { mutableStateOf(true) }
    BlindarTheme {
        Column(modifier = Modifier.background(MaterialTheme.colorScheme.surface)) {
            BlindarChip(
                text = "로그아웃",
                enabled = enabled,
                onClick = { enabled = !enabled },
            )
        }
    }
}