package com.practice.verifyphone

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.practice.designsystem.DarkPreview
import com.practice.designsystem.LightPreview
import com.practice.designsystem.components.BlindarChip
import com.practice.designsystem.components.BlindarLargeTopAppBar
import com.practice.designsystem.components.BlindarTopAppBarDefaults
import com.practice.designsystem.components.BottomNextButton
import com.practice.designsystem.components.LabelSmall
import com.practice.designsystem.components.TitleSmall
import com.practice.designsystem.theme.BlindarTheme
import com.practice.util.LocalActivity
import com.practice.util.makeToast

@Composable
fun VerifyPhoneNumber(
    onBackButtonClick: () -> Unit,
    onExistingUserLogin: () -> Unit,
    onUsernameNotSet: () -> Unit,
    onSchoolNotSelected: () -> Unit,
    onNewUserSignUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: VerifyPhoneViewModel = hiltViewModel()
) {
    val systemUiController = rememberSystemUiController()
    val navigationBarColor = MaterialTheme.colorScheme.surfaceVariant
    LaunchedEffect(true) {
        systemUiController.setNavigationBarColor(navigationBarColor)
    }

    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val activity = LocalActivity.current
    val context = LocalContext.current

    val onCodeInvalid = { context.makeToast(R.string.invalid_code) }
    val verifyAuthCode = {
        viewModel.verifyAuthCode(
            activity = activity,
            onNewUserSignUp = onNewUserSignUp,
            onExistingUserLogin = onExistingUserLogin,
            onUsernameNotSet = onUsernameNotSet,
            onSchoolNotSelected = onSchoolNotSelected,
            onCodeInvalid = onCodeInvalid,
        )
    }

    VerifyPhoneNumber(
        state = state,
        onBackButtonClick = onBackButtonClick,
        onPhoneNumberChange = viewModel::onPhoneNumberChange,
        onAuthChipClick = {
            viewModel.onAuthChipClick(
                activity = activity,
                onCodeSent = { context.makeToast(R.string.auth_code_sent) },
                onNewUserSignUp = onNewUserSignUp,
                onExistingUserLogin = onExistingUserLogin,
                onUsernameNotSet = onUsernameNotSet,
                onSchoolNotSelected = onSchoolNotSelected,
                onCodeInvalid = onCodeInvalid,
                onFail = { context.makeToast(R.string.auth_code_fail) },
            )
        },
        onAuthCodeChange = viewModel::onAuthCodeChange,
        onVerifyAuthCode = verifyAuthCode,
        isAuthCodeInvalid = state.isAuthCodeInvalid,
        modifier = modifier,
    )
}

@Composable
private fun VerifyPhoneNumber(
    state: VerifyPhoneUiState,
    onBackButtonClick: () -> Unit,
    onPhoneNumberChange: (String) -> Unit,
    onAuthChipClick: () -> Unit,
    onAuthCodeChange: (String) -> Unit,
    onVerifyAuthCode: () -> Unit,
    isAuthCodeInvalid: Boolean,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current
    val hideKeyboardAndVerifyAuthCode = {
        focusManager.clearFocus()
        onVerifyAuthCode()
    }

    Scaffold(
        topBar = {
            BlindarLargeTopAppBar(
                title = stringResource(id = R.string.verify_phone_screen),
                navigationIcon = {
                    BlindarTopAppBarDefaults.NavigationIcon(onClick = onBackButtonClick)
                },
                modifier = Modifier.background(MaterialTheme.colorScheme.surface),
            )
        },
        bottomBar = {
            BottomNextButton(
                text = stringResource(R.string.next_button),
                enabled = state.isVerifyCodeButtonEnabled,
                onClick = hideKeyboardAndVerifyAuthCode,
                modifier = Modifier
                    .fillMaxWidth()
            )
        },
        modifier = modifier.background(MaterialTheme.colorScheme.surface),
    ) {
        PhoneNumberCard(
            phoneNumber = state.phoneNumber,
            onPhoneNumberChange = onPhoneNumberChange,
            isPhoneNumberValid = state.isPhoneNumberValid,
            onAuthChipClick = {
                if (state.isPhoneNumberValid) {
                    onAuthChipClick()
                }
            },
            authCode = state.authCode,
            onAuthCodeChange = onAuthCodeChange,
            isAuthCodeFieldEnabled = state.isAuthCodeFieldEnabled,
            verifyAuthCode = hideKeyboardAndVerifyAuthCode,
            isAuthCodeInvalid = isAuthCodeInvalid,
            modifier = Modifier
                .padding(it)
                .fillMaxWidth()
                .widthIn(600.dp)
                .clip(RoundedCornerShape(16.dp)),
        )
    }
}

@Composable
private fun PhoneNumberCard(
    phoneNumber: String,
    onPhoneNumberChange: (String) -> Unit,
    isPhoneNumberValid: Boolean,
    onAuthChipClick: () -> Unit,
    authCode: String,
    onAuthCodeChange: (String) -> Unit,
    isAuthCodeInvalid: Boolean,
    isAuthCodeFieldEnabled: Boolean,
    verifyAuthCode: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth(),
    ) {
        PhoneNumberTextField(
            phoneNumber = phoneNumber,
            onPhoneNumberChange = onPhoneNumberChange,
            isValid = isPhoneNumberValid,
            onAuthChipClick = onAuthChipClick,
            modifier = Modifier.fillMaxWidth(),
        )
        AuthCodeTextField(
            code = authCode,
            onValueChange = onAuthCodeChange,
            enabled = isAuthCodeFieldEnabled,
            isError = isAuthCodeInvalid,
            verifyAuthCode = verifyAuthCode,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun PhoneNumberTextField(
    phoneNumber: String,
    onPhoneNumberChange: (String) -> Unit,
    isValid: Boolean,
    onAuthChipClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isError = phoneNumber.isNotEmpty() && !isValid
    OutlinedTextField(
        value = phoneNumber,
        onValueChange = onPhoneNumberChange,
        modifier = modifier,
        label = {
            TitleSmall(text = stringResource(id = R.string.phone_number_label))
        },
        isError = isError,
        supportingText = {
            if (isError) {
                LabelSmall(text = stringResource(R.string.invalid_phone_number))
            }
        },
        trailingIcon = {
            BlindarChip(
                text = stringResource(R.string.auth_chip_label),
                enabled = isValid,
                onClick = onAuthChipClick,
                modifier = Modifier.padding(end = 12.dp),
            )
        },
        textStyle = MaterialTheme.typography.titleMedium,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Phone,
            imeAction = ImeAction.Send
        ),
        keyboardActions = KeyboardActions(
            onSend = {
                onAuthChipClick()
            },
        ),
        singleLine = true,
    )
}

@Composable
private fun AuthCodeTextField(
    code: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean,
    isError: Boolean,
    verifyAuthCode: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    OutlinedTextField(
        value = code,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        label = {
            TitleSmall(text = stringResource(R.string.auth_code_field_label))
        },
        isError = isError,
        supportingText = {
            if (isError) {
                LabelSmall(text = stringResource(id = R.string.invalid_code))
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Send,
        ),
        keyboardActions = KeyboardActions(
            onSend = {
                keyboardController?.hide()
                verifyAuthCode()
            },
        ),
        singleLine = true,
    )
}

@DarkPreview
@Composable
private fun VerifyPhoneNumberPreview() {
    var registerState by remember { mutableStateOf(VerifyPhoneUiState.Empty) }
    BlindarTheme {
        VerifyPhoneNumber(
            state = registerState,
            onBackButtonClick = { },
            onPhoneNumberChange = { registerState = registerState.copy(phoneNumber = it) },
            onAuthChipClick = { registerState = registerState.copy(isAuthCodeFieldEnabled = true) },
            onAuthCodeChange = { registerState = registerState.copy(authCode = it) },
            onVerifyAuthCode = { },
            isAuthCodeInvalid = registerState.authCode.let { it.isNotEmpty() && it.length != 6 },
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@LightPreview
@Composable
private fun PhoneNumberCardPreview() {
    var phoneNumber by remember { mutableStateOf("") }
    val isPhoneNumberValid by remember {
        derivedStateOf {
            PhoneNumberValidator.validate(phoneNumber)
        }
    }
    var authCode by remember { mutableStateOf("") }
    var isAuthCodeEnabled by remember { mutableStateOf(false) }
    BlindarTheme {
        PhoneNumberCard(
            phoneNumber = phoneNumber,
            onPhoneNumberChange = { phoneNumber = it },
            isPhoneNumberValid = isPhoneNumberValid,
            onAuthChipClick = { isAuthCodeEnabled = true },
            authCode = authCode,
            onAuthCodeChange = { authCode = it },
            isAuthCodeInvalid = authCode.length != 6,
            isAuthCodeFieldEnabled = isAuthCodeEnabled,
            verifyAuthCode = { },
            modifier = Modifier.background(MaterialTheme.colorScheme.surface),
        )
    }
}