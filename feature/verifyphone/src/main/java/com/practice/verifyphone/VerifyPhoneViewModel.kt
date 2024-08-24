package com.practice.verifyphone

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.PhoneAuthProvider
import com.practice.auth.RegisterManager
import com.practice.firebase.BlindarFirebase
import com.practice.firebase.BlindarUserStatus
import com.practice.work.BlindarWorkManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class VerifyPhoneViewModel @Inject constructor(
    private val registerManager: RegisterManager,
) : ViewModel() {
    private val _uiState = MutableStateFlow(VerifyPhoneUiState.Empty)
    val uiState: StateFlow<VerifyPhoneUiState> = _uiState.asStateFlow()

    private lateinit var storedVerificationId: String
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken

    fun onPhoneNumberChange(value: String) {
        val digitsOnly = PhoneNumberValidator.filterOnlyDigits(value)
        _uiState.update {
            it.copy(phoneNumber = digitsOnly)
        }
    }

    fun onAuthChipClick(
        activity: Activity,
        onCodeSent: () -> Unit,
        onExistingUserLogin: () -> Unit,
        onUsernameNotSet: () -> Unit,
        onSchoolNotSelected: () -> Unit,
        onNewUserSignUp: () -> Unit,
        onFail: () -> Unit,
        onCodeInvalid: () -> Unit,
    ) {
        if (!uiState.value.isPhoneNumberValid) {
            return
        }
        val phoneNumberWithNationCode = "+82${uiState.value.phoneNumber.substring(1)}"
        registerManager.registerOrLoginWithPhoneNumber(
            activity = activity,
            phoneNumberWithNationCode = phoneNumberWithNationCode,
            coroutineScope = viewModelScope,
            onCodeSent = { verificationId, token ->
                onCodeSent()
                storedVerificationId = verificationId
                resendToken = token
                enableAuthCodeField()
                enableNextButton()
            },
            onNewUserSignUp = onNewUserSignUp,
            onUsernameNotSet = onUsernameNotSet,
            onSchoolNotSelected = onSchoolNotSelected,
            onExistingUserLogin = {
                onExistingUserLogin()
                BlindarWorkManager.setOneTimeFetchDataWork(activity)
            },
            onVerificationFail = onFail,
            onCodeInvalid = onCodeInvalid,
        )
    }

    private fun enableAuthCodeField() {
        _uiState.update {
            it.copy(isAuthCodeFieldEnabled = true)
        }
    }

    private fun enableNextButton() {
        _uiState.update {
            it.copy(isVerifyCodeButtonEnabled = true)
        }
    }

    fun onAuthCodeChange(value: String) {
        val digitsOnly = com.practice.verifyphone.PhoneNumberValidator.filterOnlyDigits(value)
        if (digitsOnly.length <= 6) {
            _uiState.update {
                it.copy(authCode = digitsOnly, isAuthCodeInvalid = false)
            }
        }
    }

    fun verifyAuthCode(
        activity: Activity,
        onExistingUserLogin: () -> Unit,
        onUsernameNotSet: () -> Unit,
        onSchoolNotSelected: () -> Unit,
        onNewUserSignUp: () -> Unit,
        onCodeInvalid: () -> Unit,
    ) {
        val authCode = uiState.value.authCode
        registerManager.verifyPhoneAuthCode(
            verificationId = storedVerificationId,
            code = authCode,
            activity = activity,
            coroutineScope = viewModelScope,
            onExistingUserLogin = {
                onExistingUserLogin()
                BlindarWorkManager.setOneTimeFetchDataWork(activity)

                val user = BlindarFirebase.getBlindarUser()
                if (user is BlindarUserStatus.LoginUser) {
                    BlindarWorkManager.setFetchMemoFromServerWork(activity, user.user.uid)
                }
            },
            onUsernameNotSet = onUsernameNotSet,
            onSchoolNotSelected = onSchoolNotSelected,
            onNewUserSignUp = onNewUserSignUp,
            onCodeInvalid = {
                onCodeInvalid()
                _uiState.update { it.copy(isAuthCodeInvalid = true) }
            }
        )
    }
}