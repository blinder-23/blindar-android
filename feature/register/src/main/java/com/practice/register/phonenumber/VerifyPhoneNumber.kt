package com.practice.register.phonenumber

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.practice.designsystem.LightPreview
import com.practice.designsystem.components.BlindarTopAppBar
import com.practice.designsystem.components.BlindarTopAppBarDefaults
import com.practice.designsystem.components.BottomNextButton
import com.practice.designsystem.components.LabelMedium
import com.practice.designsystem.components.LabelSmall
import com.practice.designsystem.components.TitleLarge
import com.practice.designsystem.components.TitleSmall
import com.practice.designsystem.theme.BlindarTheme
import com.practice.register.R
import com.practice.register.RegisterViewModel
import com.practice.util.LocalActivity
import com.practice.util.makeToast

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun VerifyPhoneNumber(
    onBackButtonClick: () -> Unit,
    onExistingUserLogin: () -> Unit,
    onUsernameNotSet: () -> Unit,
    onSchoolNotSelected: () -> Unit,
    onNewUserSignUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val systemUiController = rememberSystemUiController()
    val navigationBarColor = MaterialTheme.colorScheme.surfaceVariant
    LaunchedEffect(true) {
        systemUiController.setNavigationBarColor(navigationBarColor)
    }

    val state by viewModel.registerUiState
    val activity = LocalActivity.current
    val context = LocalContext.current

    val codeSentMessage = stringResource(id = R.string.auth_code_sent)
    val invalidCodeMessage = stringResource(id = R.string.invalid_code)
    val authFailMessage = stringResource(id = R.string.auth_code_fail)
    val onCodeInvalid = { context.makeToast(invalidCodeMessage) }
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

    val focusManager = LocalFocusManager.current
    val hideKeyboardAndVerifyAuthCode = {
        focusManager.clearFocus()
        verifyAuthCode()
    }

    ConstraintLayout(modifier = modifier) {
        val (appBar, phoneNumberCard, phoneNextButton) = createRefs()
        BlindarTopAppBar(
            title = stringResource(id = R.string.verify_phone_screen),
            modifier = Modifier.constrainAs(appBar) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            navigationIcon = {
                BlindarTopAppBarDefaults.NavigationIcon(onClick = onBackButtonClick)
            },
        )
        PhoneNumberCard(
            phoneNumber = state.phoneNumber,
            onPhoneNumberChange = viewModel::onPhoneNumberChange,
            isPhoneNumberValid = state.isPhoneNumberValid,
            onAuthChipClick = {
                if (state.isPhoneNumberValid) {
                    viewModel.onAuthChipClick(
                        activity = activity,
                        onCodeSent = { context.makeToast(codeSentMessage) },
                        onNewUserSignUp = onNewUserSignUp,
                        onExistingUserLogin = onExistingUserLogin,
                        onUsernameNotSet = onUsernameNotSet,
                        onSchoolNotSelected = onSchoolNotSelected,
                        onCodeInvalid = onCodeInvalid,
                        onFail = { context.makeToast(authFailMessage) },
                    )
                }
            },
            authCode = state.authCode,
            onAuthCodeChange = viewModel::onAuthCodeChange,
            isAuthCodeFieldEnabled = state.isAuthCodeFieldEnabled,
            verifyAuthCode = hideKeyboardAndVerifyAuthCode,
            isAuthCodeInvalid = viewModel.isAuthCodeInvalid,
            modifier = Modifier
                .constrainAs(phoneNumberCard) {
                    top.linkTo(appBar.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(phoneNextButton.top)
                }
                .fillMaxWidth(fraction = .8f)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surface),
        )
        BottomNextButton(
            text = stringResource(R.string.next_button),
            enabled = state.isVerifyCodeButtonEnabled,
            onClick = hideKeyboardAndVerifyAuthCode,
            modifier = Modifier
                .constrainAs(phoneNextButton) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .fillMaxWidth()
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
    ConstraintLayout(
        modifier = modifier
            .padding(16.dp),
    ) {
        val (title, phoneNumberField, authCodeField) = createRefs()
        PhoneNumberTitle(
            modifier = Modifier.constrainAs(title) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
            }
        )
        PhoneNumberTextField(
            phoneNumber = phoneNumber,
            onPhoneNumberChange = onPhoneNumberChange,
            isValid = isPhoneNumberValid,
            onAuthChipClick = onAuthChipClick,
            modifier = Modifier
                .constrainAs(phoneNumberField) {
                    start.linkTo(parent.start)
                    top.linkTo(title.bottom, margin = 20.dp)
                    end.linkTo(parent.end)
                }
                .background(MaterialTheme.colorScheme.surface)
        )
        AuthCodeTextField(
            code = authCode,
            onValueChange = onAuthCodeChange,
            enabled = isAuthCodeFieldEnabled,
            isError = isAuthCodeInvalid,
            verifyAuthCode = verifyAuthCode,
            modifier = Modifier.constrainAs(authCodeField) {
                start.linkTo(parent.start)
                top.linkTo(phoneNumberField.bottom, margin = 10.dp)
                end.linkTo(parent.end)
            }
        )
    }
}

@Composable
private fun PhoneNumberTitle(
    modifier: Modifier = Modifier,
) {
    TitleLarge(
        text = stringResource(id = R.string.phone_number_title),
        modifier = modifier,
        color = MaterialTheme.colorScheme.onSurface,
    )
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
            PhoneNumberAuthChip(
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
private fun PhoneNumberAuthChip(
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val chipColor = MaterialTheme.colorScheme.primary
    val textColor = contentColorFor(backgroundColor = chipColor)
    val disabledAlpha = 0.7f
    AssistChip(
        onClick = onClick,
        label = {
            LabelMedium(
                text = stringResource(R.string.auth_chip_label),
                color = textColor.copy(alpha = if (enabled) 1f else disabledAlpha),
            )
        },
        enabled = enabled,
        modifier = modifier,
        colors = AssistChipDefaults.assistChipColors(
            containerColor = chipColor,
            disabledContainerColor = chipColor.copy(alpha = disabledAlpha),
        ),
    )
}

@OptIn(ExperimentalComposeUiApi::class)
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

@LightPreview
@Composable
private fun PhoneNumberTextFieldPreview() {
    var phoneNumber by remember { mutableStateOf("") }
    val isPhoneNumberValid by remember {
        derivedStateOf {
            PhoneNumberValidator.validate(phoneNumber)
        }
    }
    BlindarTheme {
        PhoneNumberTextField(
            phoneNumber = phoneNumber,
            onPhoneNumberChange = { phoneNumber = it },
            isValid = isPhoneNumberValid,
            onAuthChipClick = {},
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background),
        )
    }
}

@LightPreview
@Composable
private fun PhoneNumberAuthChipPreview() {
    BlindarTheme {
        PhoneNumberAuthChip(
            enabled = true,
            onClick = {},
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
            isAuthCodeInvalid = false,
            isAuthCodeFieldEnabled = isAuthCodeEnabled,
            verifyAuthCode = { },
            modifier = Modifier.background(MaterialTheme.colorScheme.surface),
        )
    }
}