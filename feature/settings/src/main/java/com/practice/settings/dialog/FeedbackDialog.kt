package com.practice.settings.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextButton
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.practice.designsystem.LightAndDarkPreview
import com.practice.designsystem.LightPreview
import com.practice.designsystem.components.BlindarDialog
import com.practice.designsystem.components.BodySmall
import com.practice.designsystem.components.DialogTitleLarge
import com.practice.designsystem.components.LabelMedium
import com.practice.designsystem.theme.BlindarTheme
import com.practice.settings.R

@Composable
fun FeedbackDialog(
    onSend: (String, String) -> Unit, // app version name, feedback contents TODO: 매개변수 클래스 생성
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    state: FeedbackDialogState = rememberFeedbackDialogState(),
) {
    val onDismissWithState = {
        onDismiss()
        state.clear()
    }

    val shape = RoundedCornerShape(16.dp)
    BlindarDialog(
        onDismissRequest = onDismissWithState,
        modifier = modifier
            .wrapContentHeight()
            .shadow(4.dp, shape = shape)
            .clip(shape)
            .background(MaterialTheme.colorScheme.surface)
    ) {
        FeedbackDialogContent(
            onSend = onSend,
            onDismiss = onDismissWithState,
            state = state,
        )
    }
}

@Composable
private fun FeedbackDialogContent(
    onSend: (String, String) -> Unit,
    onDismiss: () -> Unit,
    state: FeedbackDialogState,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val textColor = contentColorFor(backgroundColor = MaterialTheme.colorScheme.surface)

    LazyColumn(
        modifier = modifier,
    ) {
        item {
            DialogTitleLarge(
                text = stringResource(id = R.string.feedback_dialog_title),
                color = textColor,
                modifier = Modifier.padding(16.dp)
            )
        }
        item {
            FeedbackTextField(
                state = state,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
        }
        item {
            FeedbackDialogButtons(
                onSend = { state.sendOnlyWhenTextIsNotEmpty(context, onSend) },
                onDismiss = onDismiss,
            )
        }
    }
}

@Composable
private fun FeedbackTextField(
    state: FeedbackDialogState,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current
    val errorTextAlpha = if (state.isError) 1f else 0f
    OutlinedTextField(
        value = state.feedbackText,
        onValueChange = state::onFeedbackTextUpdate,
        isError = state.isError,
        supportingText = {
            LabelMedium(
                text = stringResource(id = R.string.feedback_dialog_textfield_error_empty),
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.alpha(errorTextAlpha),
            )
        },
        label = {
            LabelMedium(text = stringResource(id = R.string.feedback_dialog_textfield_label))
        },
        minLines = 4,
        maxLines = 4,
        colors = OutlinedTextFieldDefaults.colors(),
        modifier = modifier.fillMaxWidth(),
        keyboardActions = KeyboardActions(
            onDone = { focusManager.clearFocus() },
        ),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
    )
}

@Composable
private fun FeedbackDialogButtons(
    onSend: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier.wrapContentHeight()) {
        FeedbackDialogButton(
            text = stringResource(id = R.string.feedback_dialog_cancel_button),
            onClick = onDismiss,
            modifier = Modifier.weight(1f),
        )
        FeedbackDialogButton(
            text = stringResource(id = R.string.feedback_dialog_send_button),
            onClick = onSend,
            modifier = Modifier
                .weight(1f)
                .background(MaterialTheme.colorScheme.primaryContainer),
        )
    }
}

@Composable
private fun FeedbackDialogButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TextButton(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(0.dp),
    ) {
        BodySmall(text = text)
    }
}

@LightAndDarkPreview
@Composable
private fun FeedbackDialogPreview() {
    BlindarTheme {
        FeedbackDialog(
            onSend = { _, _ ->

            },
            onDismiss = { },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
        )
    }
}

@LightPreview
@Composable
private fun FeedbackDialogErrorPreview() {
    BlindarTheme {
        FeedbackDialog(
            onSend = { _, _ ->

            },
            onDismiss = { },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            state = rememberFeedbackDialogState(isError = true),
        )
    }
}