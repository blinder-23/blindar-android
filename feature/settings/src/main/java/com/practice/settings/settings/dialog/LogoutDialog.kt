package com.practice.settings.settings.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.practice.designsystem.DarkPreview
import com.practice.designsystem.components.BlindarDialog
import com.practice.designsystem.components.BodyLarge
import com.practice.designsystem.components.DialogTitleMedium
import com.practice.designsystem.theme.BlindarTheme
import com.practice.settings.R

@Composable
fun LogoutDialog(
    onDismiss: () -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(16.dp)

    BlindarDialog(
        onDismissRequest = onDismiss,
        modifier = modifier
            .shadow(4.dp, shape = shape)
            .clip(shape)
            .background(MaterialTheme.colorScheme.surface),
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            DialogTitleMedium(
                text = stringResource(id = R.string.settings_logout_dialog_title),
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(16.dp),
            )
            Row(modifier = Modifier.fillMaxWidth()) {
                BaseLogoutDialogButton(
                    text = stringResource(id = R.string.settings_logout_dialog_dismiss),
                    onClick = onDismiss,
                    background = MaterialTheme.colorScheme.surface,
                    modifier = Modifier.weight(1f),
                )
                BaseLogoutDialogButton(
                    text = stringResource(id = R.string.settings_logout_dialog_logout),
                    onClick = onLogout,
                    background = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@Composable
private fun BaseLogoutDialogButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    background: Color = MaterialTheme.colorScheme.primaryContainer,
) {
    val textColor = contentColorFor(backgroundColor = background)
    Box(
        modifier = modifier
            .clickable(onClick = onClick)
            .semantics {
                role = Role.Button
            }
            .background(background),
    ) {
        BodyLarge(
            text = text,
            color = textColor,
            modifier = Modifier
                .padding(vertical = 16.dp)
                .align(Alignment.Center),
        )
    }
}

@DarkPreview
@Composable
private fun LogoutDialogPreview() {
    BlindarTheme {
        LogoutDialog(
            onDismiss = {},
            onLogout = {},
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
        )
    }
}