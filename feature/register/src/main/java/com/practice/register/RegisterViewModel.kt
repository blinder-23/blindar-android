package com.practice.register

import android.app.Activity
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.practice.api.school.RemoteSchoolRepository
import com.practice.firebase.BlindarFirebase
import com.practice.preferences.PreferencesRepository
import com.practice.register.selectschool.School
import com.practice.util.update
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val schoolRepository: RemoteSchoolRepository,
    private val preferencesRepository: PreferencesRepository,
) : ViewModel() {
    private val TAG = "RegisterViewModel"
    var registerUiState = mutableStateOf(RegisterUiState.Empty)
        private set

    private var schoolListJob: Job? = null

    private lateinit var storedVerificationId: String
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken

    init {
        updateSchoolList("")
    }

    fun onPhoneNumberChange(value: String) {
        registerUiState.update {
            this.copy(phoneNumber = value)
        }
    }

    fun onAuthChipClick(
        activity: Activity,
        onCodeSent: () -> Unit,
        onExistingUserLogin: () -> Unit,
        onNewUserSignUp: () -> Unit,
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
                    Log.d(TAG, "onVerification complete")
                    BlindarFirebase.signInWithPhoneAuthCredential(
                        activity = activity,
                        credential = credential,
                        onExistingUserLogin = onExistingUserLogin,
                        onNewUserSignUp = onNewUserSignUp,
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
        onExistingUserLogin: () -> Unit,
        onNewUserSignUp: () -> Unit,
        onCodeInvalid: () -> Unit,
    ) {
        val authCode = registerUiState.value.authCode
        val credential = PhoneAuthProvider.getCredential(storedVerificationId, authCode)
        BlindarFirebase.signInWithPhoneAuthCredential(
            activity = activity,
            credential = credential,
            onExistingUserLogin = onExistingUserLogin,
            onNewUserSignUp = onNewUserSignUp,
            onCodeInvalid = onCodeInvalid
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
        Log.d(TAG, "name: ${registerUiState.value.name}, valid: ${registerUiState.value.isNameValid}")
        if (registerUiState.value.isNameValid) {
            BlindarFirebase.tryStoreUsername(
                username = registerUiState.value.name,
                onSuccess = onSuccess,
                onFail = onFail,
            )
        } else {
            onFail()
        }
    }

    /**
     * SelectSchoolScreen
     */
    fun onSchoolQueryChange(query: String) {
        registerUiState.update {
            this.copy(schoolQuery = query)
        }
        updateSchoolList(query)
    }

    private fun updateSchoolList(query: String) {
        schoolListJob?.cancel()
        schoolListJob = viewModelScope.launch {
            val schools = schoolRepository.searchSupportedSchools(query)
                .map { it.toSchool() }
                .toImmutableList()
            registerUiState.update {
                this.copy(schools = schools)
            }
        }
    }

    fun onSchoolClick(
        school: School,
        onSuccess: () -> Unit,
        onFail: () -> Unit
    ) {
        viewModelScope.launch {
            preferencesRepository.updateSchoolId(school.schoolId)
        }
        BlindarFirebase.tryUpdateCurrentUserSchoolId(
            schoolId = school.schoolId,
            onSuccess = onSuccess,
            onFail = onFail,
        )
    }
}