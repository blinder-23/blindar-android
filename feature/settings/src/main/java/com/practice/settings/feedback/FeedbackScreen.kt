package com.practice.settings.feedback

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.practice.designsystem.DarkPreview
import com.practice.designsystem.components.BlindarButton
import com.practice.designsystem.components.BlindarTopAppBar
import com.practice.designsystem.components.BlindarTopAppBarDefaults
import com.practice.designsystem.components.BodyLarge
import com.practice.designsystem.components.LabelMedium
import com.practice.designsystem.theme.BlindarTheme
import com.practice.settings.R
import com.practice.util.getAppVersionName
import com.practice.util.makeToast
import kotlinx.coroutines.launch

@Composable
fun FeedbackScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: FeedbackViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val successMessage = stringResource(id = R.string.settings_send_feedback_on_success)
    val failMessage = stringResource(id = R.string.settings_send_feedback_on_error)
    val feedbackEmptyMessage = stringResource(id = R.string.feedback_screen_textfield_error_empty)

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    FeedbackScreen(
        onNavigateBack = onNavigateBack,
        feedback = uiState.feedback,
        onFeedbackUpdate = viewModel::updateFeedback,
        isError = uiState.isError,
        onSend = { request ->
            coroutineScope.launch {
                val message = when {
                    !viewModel.canSendFeedback(request.feedback) -> feedbackEmptyMessage
                    viewModel.sendFeedback(request) -> {
                        onNavigateBack()
                        successMessage
                    }

                    else -> failMessage
                }
                context.makeToast(message)
            }
        },
        onCancel = onNavigateBack,
        modifier = modifier,
    )
}

@Composable
private fun FeedbackScreen(
    onNavigateBack: () -> Unit,
    feedback: String,
    onFeedbackUpdate: (String) -> Unit,
    isError: Boolean,
    onSend: (UiFeedbackRequest) -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val versionName = LocalContext.current.getAppVersionName()

    Scaffold(
        topBar = {
            BlindarTopAppBar(
                title = stringResource(id = R.string.feedback_screen_title),
                navigationIcon = { BlindarTopAppBarDefaults.NavigationIcon(onNavigateBack) },
            )
        },
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(horizontal = 16.dp)
        ) {
            FeedbackTextField(
                feedback = feedback,
                onFeedbackUpdate = onFeedbackUpdate,
                isError = isError,
                modifier = Modifier
                    .fillMaxWidth(),
            )
            FeedbackButtons(
                onSend = { onSend(UiFeedbackRequest(feedback, versionName)) },
                onDismiss = onCancel,
            )
        }
    }
}

@Composable
private fun FeedbackTextField(
    feedback: String,
    onFeedbackUpdate: (String) -> Unit,
    isError: Boolean,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current
    val errorTextAlpha = if (isError) 1f else 0f
    OutlinedTextField(
        value = feedback,
        onValueChange = onFeedbackUpdate,
        isError = isError,
        supportingText = {
            LabelMedium(
                text = stringResource(id = R.string.feedback_screen_textfield_error_empty),
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.alpha(errorTextAlpha),
            )
        },
        label = {
            LabelMedium(text = stringResource(id = R.string.feedback_screen_textfield_label))
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
private fun FeedbackButtons(
    onSend: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.wrapContentHeight(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        FeedbackDialogButton(
            text = stringResource(id = R.string.feedback_dialog_cancel_button),
            onClick = onDismiss,
            isPrimary = false,
            modifier = Modifier.weight(1f),
        )
        FeedbackDialogButton(
            text = stringResource(id = R.string.feedback_dialog_send_button),
            onClick = onSend,
            isPrimary = true,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun FeedbackDialogButton(
    text: String,
    onClick: () -> Unit,
    isPrimary: Boolean,
    modifier: Modifier = Modifier,
) {
    BlindarButton(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        isPrimary = isPrimary,
    ) {
        BodyLarge(
            text = text,
            modifier = Modifier.padding(vertical = 4.dp),
        )
    }
}

@DarkPreview
@Composable
private fun FeedbackScreenPreview() {
    var feedback by remember { mutableStateOf("") }
    BlindarTheme {
        FeedbackScreen(
            feedback = feedback,
            onFeedbackUpdate = { feedback = it },
            onNavigateBack = {},
            isError = false,
            onSend = {},
            onCancel = {},
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .fillMaxSize(),
        )
    }
}