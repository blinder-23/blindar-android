package com.practice.designsystem.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.practice.designsystem.LightPreview
import com.practice.designsystem.R
import com.practice.designsystem.theme.BlindarTheme

@Composable
fun AppIcon(
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
) {
    Image(
        painter = painterResource(id = R.drawable.blindar_icon),
        contentDescription = contentDescription,
        modifier = modifier,
    )
}

@LightPreview
@Composable
private fun AppIconPreview() {
    BlindarTheme {
        AppIcon(
            modifier = Modifier
                .padding(16.dp)
                .size(100.dp)
                .background(MaterialTheme.colorScheme.surface),
        )
    }
}