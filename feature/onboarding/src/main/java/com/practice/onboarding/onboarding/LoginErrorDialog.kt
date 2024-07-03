package com.practice.onboarding.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.practice.designsystem.DarkPreview
import com.practice.designsystem.components.BlindarButton
import com.practice.designsystem.components.BlindarDialog
import com.practice.designsystem.components.BodyLarge
import com.practice.designsystem.components.TitleMedium
import com.practice.designsystem.theme.BlindarTheme
import com.practice.onboarding.R

@Composable
fun LoginErrorDialog(
    isSendButtonEnabled: Boolean,
    onDismiss: () -> Unit,
    onSendLog: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BlindarDialog(
        onDismissRequest = onDismiss,
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface),
    ) {
        Column {
            CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onSurface) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    TitleMedium(text = stringResource(id = R.string.login_error_dialog_title))
                    BodyLarge(text = stringResource(id = R.string.login_error_dialog_body))
                    BodyLarge(text = stringResource(id = R.string.login_error_dialog_caption))
                }
            }
            LoginErrorDialogButtons(
                isSendButtonEnabled = isSendButtonEnabled,
                onCancel = onDismiss,
                onConfirm = onSendLog,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun LoginErrorDialogButtons(
    isSendButtonEnabled: Boolean,
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier) {
        BlindarButton(
            onClick = onCancel,
            modifier = Modifier.weight(1f),
            isPrimary = false,
        ) {
            BodyLarge(
                text = stringResource(id = R.string.login_error_dialog_cancel),
                modifier = Modifier.padding(6.dp),
            )
        }
        BlindarButton(
            onClick = onConfirm,
            modifier = Modifier.weight(1f),
            enabled = isSendButtonEnabled,
        ) {
            BodyLarge(
                text = stringResource(id = R.string.login_error_dialog_confirm),
                modifier = Modifier.padding(6.dp),
            )
        }
    }
}

@DarkPreview
@Composable
private fun LoginErrorDialogPreview() {
    BlindarTheme {
        LoginErrorDialog(
            isSendButtonEnabled = true,
            onDismiss = {},
            onSendLog = {},
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
        )
    }
}