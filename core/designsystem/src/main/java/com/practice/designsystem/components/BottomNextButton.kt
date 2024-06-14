package com.practice.designsystem.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.practice.designsystem.DarkPreview
import com.practice.designsystem.theme.BlindarTheme

@Composable
fun BottomNextButton(
    text: String,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val round = 16.dp
    val alpha by animateFloatAsState(
        targetValue = if (enabled) 1f else 0.4f,
        label = "BottomNextButton alpha",
    )

    TextButton(
        onClick = onClick,
        modifier = modifier
            .clip(RoundedCornerShape(topStart = round, topEnd = round))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = alpha))
            .padding(vertical = 8.dp),
        enabled = enabled,
    ) {
        TitleLarge(
            text = text,
            modifier = Modifier.align(Alignment.CenterVertically),
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = alpha),
        )
    }
}

@DarkPreview
@Composable
private fun BottomNextButtonPreview() {
    var enabled by remember { mutableStateOf(true) }
    BlindarTheme {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            TextButton(
                onClick = { enabled = !enabled },
                modifier = Modifier.border(color = MaterialTheme.colorScheme.primary, width = 1.dp),
            ) {
                LabelSmall(text = "enabled: $enabled")
            }
            BottomNextButton(
                text = "다음",
                enabled = enabled,
                onClick = { },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}