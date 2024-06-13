package com.practice.designsystem.components

import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.DialogProperties

/**
 * Modal popup of the blindar app.
 *
 * This composable automatically shows opaque background.
 * [content] will be shown at the center of the screen.
 *
 * @param onDismissRequest called when the user clicks outside the dialog or on the back
 * @param modifier modifiers to set to the dialog container
 * @param properties properties to set to the dialog
 * @param content composable which will be shown as a dialog content
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BlindarDialog(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    properties: DialogProperties = DialogProperties(),
    content: @Composable () -> Unit,
) {
    BasicAlertDialog(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        properties = properties,
        content = content,
    )
}