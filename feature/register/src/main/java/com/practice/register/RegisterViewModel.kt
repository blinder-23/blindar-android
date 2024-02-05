package com.practice.register

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.PhoneAuthProvider
import com.practice.api.school.RemoteSchoolRepository
import com.practice.api.toSchool
import com.practice.auth.RegisterManager
import com.practice.domain.School
import com.practice.firebase.BlindarFirebase
import com.practice.firebase.BlindarUserStatus
import com.practice.preferences.PreferencesRepository
import com.practice.register.phonenumber.PhoneNumberValidator
import com.practice.util.removeWhitespaces
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
        val phoneNumberWithNationCode = "+82${registerUiState.value.phoneNumber.substring(1)}"
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
        registerManager.verifyPhoneAuthCode(
            verificationId = storedVerificationId,
            code = authCode,
            activity = activity,
            coroutineScope = viewModelScope,
            onExistingUserLogin = {
                onExistingUserLogin()
                BlindarWorkManager.setOneTimeFetchDataWork(activity)

                val user = BlindarFirebase.getBlindarUser()
                Log.d(TAG, "is user already registered? ${user is BlindarUserStatus.LoginUser}")
                if (user is BlindarUserStatus.LoginUser) {
                    Log.d(TAG, "load memo from server of user")
                    BlindarWorkManager.setFetchMemoFromServerWork(activity, user.user.uid)
                }
            },
            onUsernameNotSet = onUsernameNotSet,
            onSchoolNotSelected = onSchoolNotSelected,
            onNewUserSignUp = onNewUserSignUp,
            onCodeInvalid = onCodeInvalid
        )
    }

    /**
     * RegisterFormScreen
     */
    fun onNameChange(name: String) {
        registerUiState.update {
            this.copy(name = name.removeWhitespaces())
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
        val queryWithoutWhitespaces = query.removeWhitespaces()
        registerUiState.update {
            this.copy(schoolQuery = queryWithoutWhitespaces)
        }
        updateSchoolList(queryWithoutWhitespaces)
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
        onSchoolClickCallbackFromUI: () -> Unit,
    ) {
        viewModelScope.launch {
            preferencesRepository.updateSelectedSchool(school.schoolCode, school.name)
            BlindarWorkManager.setOneTimeFetchDataWork(context)
            BlindarWorkManager.setUserInfoToRemoteWork(context)
            onSchoolClickCallbackFromUI()
        }
    }
}