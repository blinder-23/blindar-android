package com.practice.designsystem.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.practice.designsystem.LightAndDarkPreview
import com.practice.designsystem.R
import com.practice.designsystem.theme.BlindarTheme
import com.practice.designsystem.theme.NanumSquareRound

@Composable
fun AppIcon(
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(id = R.drawable.blindar_icon),
            contentDescription = contentDescription,
        )
        TitleMedium(
            text = "CALENDAR FOR BLIND",
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(top = 8.dp),
            fontFamily = NanumSquareRound,
            fontWeight = FontWeight.Bold,
        )
        DisplayMedium(
            text = "BLINDAR",
            color = MaterialTheme.colorScheme.primary,
            fontFamily = NanumSquareRound,
            fontWeight = FontWeight.ExtraBold,
        )
    }
}

@LightAndDarkPreview
@Composable
private fun AppIconPreview() {
    BlindarTheme {
        AppIcon(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp),
        )
    }
}