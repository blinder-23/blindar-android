package com.practice.register

import android.app.Activity
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.practice.firebase.BlindarFirebase
import com.practice.util.update
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor() : ViewModel() {
    private val TAG = "RegisterViewModel"
    var registerUiState = mutableStateOf(RegisterUiState.Empty)
        private set

    private lateinit var storedVerificationId: String
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken

    fun onPhoneNumberChange(value: String) {
        registerUiState.update {
            this.copy(phoneNumber = value)
        }
    }

    fun onAuthChipClick(
        activity: Activity,
        onCodeSent: ()->Unit,
        onSuccess: () -> Unit,
        onFail: () -> Unit,
        onCodeInvalid: () -> Unit,
    ) {
        if (!registerUiState.value.isPhoneNumberValid) {
            return
        }
        val phoneNumber = "+82${registerUiState.value.phoneNumber.substring(1)}"
        BlindarFirebase.signUpWithPhoneNumber(
            activity = activity,
            phoneNumber = phoneNumber,
            callback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    BlindarFirebase.signInWithPhoneAuthCredential(
                        activity = activity,
                        credential = credential,
                        onSuccess = onSuccess,
                        onCodeInvalid = onCodeInvalid
                    )
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    Log.e(TAG, "onVerificationFailed", e)
                    onVerificationFail(e, onFail)
                }

                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
                ) {
                    Log.d(TAG, "id: $verificationId, token: $token")
                    onCodeSent()
                    storedVerificationId = verificationId
                    resendToken = token
                    enableAuthCodeField()
                    enableNextButton()
                }

                override fun onCodeAutoRetrievalTimeOut(p0: String) {
                    // TODO: show seconds left
                    Log.e(TAG, "code timeout! $p0")
                    super.onCodeAutoRetrievalTimeOut(p0)
                }
            }
        )
    }

    private fun onVerificationFail(
        e: FirebaseException,
        onFail: () -> Unit,
    ) {
        when (e) {
            is FirebaseAuthInvalidCredentialsException -> {
                Log.e(TAG, "Invalid request")
            }

            is FirebaseTooManyRequestsException -> {
                Log.e(TAG, "Firebase quota exceeded")
            }

            is FirebaseAuthMissingActivityForRecaptchaException -> {
                Log.e(TAG, "reCAPTCHA verification attempted with null activity")
            }

            else -> {
                Log.e(TAG, "Unknown error", e)
            }
        }
        onFail()
    }

    private fun enableAuthCodeField() {
        registerUiState.update {
            this.copy(isAuthCodeFieldEnabled = true)
        }
    }

    private fun enableNextButton() {
        registerUiState.update {
            this.copy(isVerifyCodeButtonEnabled = true)
        }
    }

    fun onAuthCodeChange(value: String) {
        if (value.length <= 6) {
            registerUiState.update {
                this.copy(authCode = value)
            }
        }
    }

    fun verifyAuthCode(
        activity: Activity,
        onSuccess: () -> Unit,
        onCodeInvalid: () -> Unit,
    ) {
        val authCode = registerUiState.value.authCode
        val credential = PhoneAuthProvider.getCredential(storedVerificationId, authCode)
        BlindarFirebase.signInWithPhoneAuthCredential(
            activity,
            credential,
            onSuccess,
            onCodeInvalid
        )
    }

    /**
     * RegisterFormScreen
     */
    fun onNameChange(name: String) {
        registerUiState.update {
            this.copy(name = name)
        }
    }

    fun submitName(
        onSuccess: () -> Unit,
        onFail: () -> Unit,
    ) {
        val name = registerUiState.value.name
        BlindarFirebase.tryUpdateCurrentUsername(
            username = name,
            onSuccess = onSuccess,
            onFail = onFail,
        )
    }
}