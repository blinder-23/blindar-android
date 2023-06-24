package com.practice.designsystem.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.unit.dp
import com.practice.designsystem.LightAndDarkPreview
import com.practice.designsystem.R
import com.practice.designsystem.theme.BlindarTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BlindarTopAppBar(
    title: String,
    onBackButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val round = 16.dp
    CenterAlignedTopAppBar(
        modifier = modifier
            .clip(RoundedCornerShape(bottomStart = round, bottomEnd = round)),
        title = {
            TitleLarge(
                text = title,
                modifier = Modifier.clearAndSetSemantics { contentDescription = "$title 화면" }
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackButtonClick) {
                Icon(
                    painter = painterResource(R.drawable.arrow_back),
                    contentDescription = stringResource(id = R.string.back_arrow),
                    tint = MaterialTheme.colorScheme.primary,
                )
            }
        }
    )
}

@LightAndDarkPreview
@Composable
private fun TopAppBarPreview() {
    BlindarTheme {
        BlindarTopAppBar(
            title = "휴대폰 인증",
            onBackButtonClick = {},
            modifier = Modifier.fillMaxWidth(),
        )
    }
}