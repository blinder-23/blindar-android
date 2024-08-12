package com.practice.register.registerform

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.practice.designsystem.DarkPreview
import com.practice.designsystem.components.BlindarLargeTopAppBar
import com.practice.designsystem.components.BlindarTopAppBarDefaults
import com.practice.designsystem.components.BottomNextButton
import com.practice.designsystem.components.LabelSmall
import com.practice.designsystem.theme.BlindarTheme
import com.practice.register.R
import com.practice.register.RegisterUiState
import com.practice.register.RegisterViewModel
import com.practice.util.makeToast

@Composable
fun RegisterFormScreen(
    onBackButtonClick: () -> Unit,
    onNameUpdated: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val state by viewModel.registerUiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    RegisterFormScreen(
        state = state,
        onBackButtonClick = onBackButtonClick,
        onSubmitName = {
            viewModel.submitName(
                onSuccess = onNameUpdated,
                onFail = { context.makeToast(R.string.username_duplicate) },
            )
        },
        onNameChange = viewModel::onNameChange,
        modifier = modifier,
    )
}

@Composable
private fun RegisterFormScreen(
    state: RegisterUiState,
    onBackButtonClick: () -> Unit,
    onSubmitName: () -> Unit,
    onNameChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val textFieldFocusRequester = remember { FocusRequester() }
    Scaffold(
        topBar = {
            BlindarLargeTopAppBar(
                title = stringResource(id = R.string.register_form_screen),
                navigationIcon = {
                    BlindarTopAppBarDefaults.NavigationIcon(onClick = onBackButtonClick)
                },
            )
        },
        bottomBar = {
            BottomNextButton(
                text = stringResource(R.string.next_button),
                enabled = true,
                onClick = onSubmitName,
                modifier = Modifier.fillMaxWidth(),
            )
        },
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        RegisterNameCard(
            name = state.name,
            onNameChange = onNameChange,
            isValid = state.name.isEmpty() || state.isNameValid,
            isDuplicate = state.isDuplicateName,
            submitName = onSubmitName,
            focusRequester = textFieldFocusRequester,
            modifier = Modifier
                .padding(it)
                .fillMaxWidth()
                .widthIn(600.dp)
                .background(MaterialTheme.colorScheme.surface),
        )
    }
}

@Composable
private fun RegisterNameCard(
    name: String,
    onNameChange: (String) -> Unit,
    isValid: Boolean,
    isDuplicate: Boolean,
    submitName: () -> Unit,
    focusRequester: FocusRequester,
    modifier: Modifier = Modifier,
) {
    NameTextField(
        name = name,
        onNameChange = onNameChange,
        isInvalid = !isValid,
        isDuplicateName = isDuplicate,
        submitName = submitName,
        modifier = modifier
            .padding(16.dp)
            .focusRequester(focusRequester),
    )
}

@Composable
private fun NameTextField(
    name: String,
    onNameChange: (String) -> Unit,
    isInvalid: Boolean,
    isDuplicateName: Boolean,
    submitName: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    OutlinedTextField(
        value = name,
        onValueChange = onNameChange,
        modifier = modifier,
        isError = isInvalid || isDuplicateName,
        placeholder = {
            LabelSmall(text = stringResource(id = R.string.name_placeholder))
        },
        supportingText = {
            if (isDuplicateName) {
                LabelSmall(text = stringResource(id = R.string.username_duplicate))
            } else if (isInvalid) {
                LabelSmall(text = stringResource(id = R.string.username_invalid))
            }
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
        keyboardActions = KeyboardActions(
            onSend = {
                keyboardController?.hide()
                submitName()
            }
        ),
        singleLine = true,
    )
}

@DarkPreview
@Composable
private fun RegisterFormScreenPreview() {
    var state by remember { mutableStateOf(RegisterUiState.Empty) }
    BlindarTheme {
        RegisterFormScreen(
            state = state,
            onBackButtonClick = {},
            onNameChange = { state = state.copy(name = it, isDuplicateName = false) },
            onSubmitName = { state = state.copy(isDuplicateName = state.name == "중복") },
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary),
        )
    }
}