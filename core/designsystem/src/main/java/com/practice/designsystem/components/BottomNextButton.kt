package com.practice.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.practice.designsystem.LightAndDarkPreview
import com.practice.designsystem.theme.BlindarTheme

@Composable
fun BottomNextButton(
    text: String,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val round = 16.dp
    TextButton(
        onClick = onClick,
        modifier = modifier
            .clip(RoundedCornerShape(topStart = round, topEnd = round))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(vertical = 8.dp),
        enabled = enabled,
    ) {
        TitleLarge(
            text = text,
            modifier = Modifier.align(Alignment.CenterVertically),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@LightAndDarkPreview
@Composable
private fun BottomNextButtonPreview() {
    BlindarTheme {
        BottomNextButton(
            text = "다음",
            enabled = true,
            onClick = {},
            modifier = Modifier.fillMaxWidth(),
        )
    }
}