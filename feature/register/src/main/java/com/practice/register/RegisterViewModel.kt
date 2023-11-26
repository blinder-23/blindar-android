package com.practice.register

import android.app.Activity
import android.content.Context
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
import com.practice.api.toSchool
import com.practice.domain.School
import com.practice.firebase.BlindarFirebase
import com.practice.preferences.PreferencesRepository
import com.practice.register.phonenumber.PhoneNumberValidator
import com.practice.user.RegisterManager
import com.practice.user.UserRegisterState
import com.practice.util.update
import com.practice.work.BlindarWorkManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val schoolRepository: RemoteSchoolRepository,
    private val preferencesRepository: PreferencesRepository,
    private val registerManager: RegisterManager,
) : ViewModel() {
    private val TAG = "RegisterViewModel"
    var registerUiState = mutableStateOf(RegisterUiState.Empty)
        private set

    private var schoolListJob: Job? = null

    private lateinit var storedVerificationId: String
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken

    init {
        viewModelScope.launch {
            collectSelectedSchool()
        }
        updateSchoolList("")
    }

    fun onPhoneNumberChange(value: String) {
        val digitsOnly = PhoneNumberValidator.filterOnlyDigits(value)
        registerUiState.update {
            this.copy(phoneNumber = digitsOnly)
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
                        onRegisterOrLoginSuccessful = {
                            onRegisterOrLoginSuccessful(
                                onNewUserSignUp = onNewUserSignUp,
                                onUsernameNotSet = onUsernameNotSet,
                                onSchoolNotSelected = onSchoolNotSelected,
                                onExistingUserLogin = onExistingUserLogin
                            )
                        },
                        onLoginFail = onCodeInvalid,
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
        val digitsOnly = PhoneNumberValidator.filterOnlyDigits(value)
        if (digitsOnly.length <= 6) {
            registerUiState.update {
                this.copy(authCode = digitsOnly)
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
        val authCode = registerUiState.value.authCode
        val credential = PhoneAuthProvider.getCredential(storedVerificationId, authCode)
        BlindarFirebase.signInWithPhoneAuthCredential(
            activity = activity,
            credential = credential,
            onRegisterOrLoginSuccessful = {
                onRegisterOrLoginSuccessful(
                    onNewUserSignUp = onNewUserSignUp,
                    onUsernameNotSet = onUsernameNotSet,
                    onSchoolNotSelected = onSchoolNotSelected,
                    onExistingUserLogin = onExistingUserLogin,
                )
            },
            onLoginFail = onCodeInvalid,
        )
    }

    private fun onRegisterOrLoginSuccessful(
        onNewUserSignUp: () -> Unit,
        onUsernameNotSet: () -> Unit,
        onSchoolNotSelected: () -> Unit,
        onExistingUserLogin: () -> Unit,
    ) {
        viewModelScope.launch {
            when (registerManager.getUserRegisterState()) {
                UserRegisterState.NOT_LOGGED_IN -> onNewUserSignUp()
                UserRegisterState.USERNAME_MISSING -> onUsernameNotSet()
                UserRegisterState.SCHOOL_NOT_SELECTED -> onSchoolNotSelected()
                UserRegisterState.ALL_FILLED -> onExistingUserLogin()
            }
        }
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
        Log.d(
            TAG,
            "name: ${registerUiState.value.name}, valid: ${registerUiState.value.isNameValid}"
        )
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
    private suspend fun collectSelectedSchool() {
        preferencesRepository.userPreferencesFlow.collectLatest { preferences ->
            registerUiState.update {
                this.copy(
                    selectedSchool = School(
                        name = preferences.schoolName,
                        schoolCode = preferences.schoolCode,
                    )
                )
            }
        }
    }

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
        context: Context,
        school: School,
        onSuccess: () -> Unit,
        onFail: () -> Unit
    ) {
        viewModelScope.launch {
            preferencesRepository.updateSelectedSchool(school.schoolCode, school.name)
            BlindarWorkManager.setOneTimeWork(context)
            BlindarWorkManager.setUserInfoToFirebaseWork(context)
        }
        BlindarFirebase.tryUpdateCurrentUserSchoolCode(
            schoolCode = school.schoolCode,
            onSuccess = onSuccess,
            onFail = onFail,
        )
    }
}