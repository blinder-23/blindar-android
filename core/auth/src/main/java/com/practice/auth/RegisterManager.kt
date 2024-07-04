package com.practice.auth

import android.app.Activity
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.practice.firebase.BlindarFirebase
import com.practice.firebase.BlindarUserStatus
import com.practice.preferences.PreferencesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

// TODO: 로그인/로그아웃 로직을 모두 여기로 모으고, BlindarFirebase에 직접 접근할 수 없도록 수정
class RegisterManager @Inject constructor(
    private val preferencesRepository: PreferencesRepository,
) {
    suspend fun getUserRegisterState(): UserRegisterState {
        val blindarUser = BlindarFirebase.getBlindarUser()
        if (blindarUser is BlindarUserStatus.NotLoggedIn) {
            return UserRegisterState.NOT_LOGGED_IN
        }

        val user = blindarUser as BlindarUserStatus.LoginUser
        return getUserRegisterState(user.user)
    }

    private suspend fun getUserRegisterState(firebaseUser: FirebaseUser): UserRegisterState {
        val username = firebaseUser.displayName
        val preferences = preferencesRepository.userPreferencesFlow.value

        return when {
            username != null && !preferences.isSchoolCodeEmpty -> UserRegisterState.AUTO_LOGIN
            username.isNullOrEmpty() -> UserRegisterState.USERNAME_MISSING
            BlindarFirebase.getSchoolCode(firebaseUser.displayName!!) == null -> UserRegisterState.SCHOOL_NOT_SELECTED
            else -> UserRegisterState.ALL_FILLED
        }
    }

    suspend fun signInWithGoogle(
        idToken: String,
        onNewUserSignUp: (FirebaseUser) -> Unit,
        onExistingUserLogin: (FirebaseUser) -> Unit,
        onFail: () -> Unit,
    ) {
        val user = BlindarFirebase.signInWithGoogle(idToken)
        checkGoogleRegisterStatus(user, onNewUserSignUp, onExistingUserLogin, onFail)
    }

    private suspend fun checkGoogleRegisterStatus(
        user: FirebaseUser?,
        onNewUserSignUp: (FirebaseUser) -> Unit,
        onExistingUserLogin: (FirebaseUser) -> Unit,
        onFail: () -> Unit,
    ) {
        when {
            user == null -> {
                // google login fail
                onFail()
            }

            getUserRegisterState(user) == UserRegisterState.ALL_FILLED -> {
                // existing user login
                storeSchoolCodeAndNameToPreferences()
                onExistingUserLogin(user)
            }

            else -> {
                // new user registers
                BlindarFirebase.storeUsername(user.displayName!!)?.addOnSuccessListener {
                    onNewUserSignUp(user)
                }?.addOnFailureListener {
                    onFail()
                }
            }
        }
    }

    fun registerOrLoginWithPhoneNumber(
        activity: Activity,
        coroutineScope: CoroutineScope,
        phoneNumberWithNationCode: String,
        onCodeSent: (String, PhoneAuthProvider.ForceResendingToken) -> Unit,
        onNewUserSignUp: () -> Unit,
        onUsernameNotSet: () -> Unit,
        onSchoolNotSelected: () -> Unit,
        onExistingUserLogin: () -> Unit,
        onVerificationFail: () -> Unit,
        onCodeInvalid: () -> Unit,
    ) {
        val callback = createPhoneAuthStateCallback(
            activity = activity,
            coroutineScope = coroutineScope,
            onCodeSent = onCodeSent,
            onNewUserSignUp = onNewUserSignUp,
            onUsernameNotSet = onUsernameNotSet,
            onSchoolNotSelected = onSchoolNotSelected,
            onExistingUserLogin = onExistingUserLogin,
            onVerificationFail = onVerificationFail,
            onCodeInvalid = onCodeInvalid
        )
        BlindarFirebase.signUpWithPhoneNumber(
            activity = activity,
            phoneNumber = phoneNumberWithNationCode,
            callback = callback
        )
    }

    private fun createPhoneAuthStateCallback(
        activity: Activity,
        coroutineScope: CoroutineScope,
        onCodeSent: (String, PhoneAuthProvider.ForceResendingToken) -> Unit,
        onNewUserSignUp: () -> Unit,
        onUsernameNotSet: () -> Unit,
        onSchoolNotSelected: () -> Unit,
        onExistingUserLogin: () -> Unit,
        onVerificationFail: () -> Unit,
        onCodeInvalid: () -> Unit,
    ) = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            signInWithPhoneAuthCredential(
                activity = activity,
                credential = credential,
                onSignInSuccess = {
                    executeByRegisterStateAfterPhoneLogin(
                        coroutineScope = coroutineScope,
                        onNewUserSignUp = onNewUserSignUp,
                        onUsernameNotSet = onUsernameNotSet,
                        onSchoolNotSelected = onSchoolNotSelected,
                        onExistingUserLogin = onExistingUserLogin,
                    )
                },
                onSignInFail = onCodeInvalid,
            )
        }

        override fun onVerificationFailed(e: FirebaseException) {
            catchVerificationFailException(onVerificationFail)
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken,
        ) {
            onCodeSent(verificationId, token)
        }

        override fun onCodeAutoRetrievalTimeOut(p0: String) {
            // TODO: UI에서 남은 초 보여주기?
            super.onCodeAutoRetrievalTimeOut(p0)
        }
    }

    private fun signInWithPhoneAuthCredential(
        activity: Activity,
        credential: PhoneAuthCredential,
        onSignInSuccess: () -> Unit,
        onSignInFail: () -> Unit,
    ) {
        BlindarFirebase.trySignInWithPhoneAuthCredential(credential)
            .addOnCompleteListener(activity) { task ->
                if (task.isSignInSuccessful) {
                    onSignInSuccess()
                } else if (task.exception is FirebaseAuthInvalidCredentialsException) {
                    onSignInFail()
                }
            }
    }

    private fun executeByRegisterStateAfterPhoneLogin(
        coroutineScope: CoroutineScope,
        onNewUserSignUp: () -> Unit,
        onUsernameNotSet: () -> Unit,
        onSchoolNotSelected: () -> Unit,
        onExistingUserLogin: () -> Unit,
    ) {
        coroutineScope.launch {
            val state = getUserRegisterState()
            when (state) {
                UserRegisterState.NOT_LOGGED_IN -> onNewUserSignUp()
                UserRegisterState.USERNAME_MISSING -> onUsernameNotSet()
                UserRegisterState.SCHOOL_NOT_SELECTED -> onSchoolNotSelected()
                UserRegisterState.ALL_FILLED -> {
                    // 다시 로그인했을 때 학교 ID, 이름을 로컬에 저장
                    storeSchoolCodeAndNameToPreferences()
                    onExistingUserLogin()
                }

                UserRegisterState.AUTO_LOGIN -> {
                    onExistingUserLogin()
                }
            }
        }
    }

    private suspend fun storeSchoolCodeAndNameToPreferences() {
        val schoolCode = BlindarFirebase.getSchoolCode()?.toInt()
        val schoolName = BlindarFirebase.getSchoolName()
        if (schoolCode != null && schoolName != null) {
            preferencesRepository.updateSelectedSchool(schoolCode, schoolName)
        }
    }

    private fun catchVerificationFailException(onFail: () -> Unit) {
        onFail()
    }

    fun verifyPhoneAuthCode(
        verificationId: String,
        code: String,
        activity: Activity,
        coroutineScope: CoroutineScope,
        onExistingUserLogin: () -> Unit,
        onUsernameNotSet: () -> Unit,
        onSchoolNotSelected: () -> Unit,
        onNewUserSignUp: () -> Unit,
        onCodeInvalid: () -> Unit,
    ) {
        val credential = PhoneAuthProvider.getCredential(verificationId, code)
        signInWithPhoneAuthCredential(
            activity = activity,
            credential = credential,
            onSignInSuccess = {
                executeByRegisterStateAfterPhoneLogin(
                    coroutineScope = coroutineScope,
                    onNewUserSignUp = onNewUserSignUp,
                    onUsernameNotSet = onUsernameNotSet,
                    onSchoolNotSelected = onSchoolNotSelected,
                    onExistingUserLogin = onExistingUserLogin,
                )
            },
            onSignInFail = onCodeInvalid,
        )
    }

    private val Task<AuthResult>.isSignInSuccessful: Boolean
        get() = this.isSuccessful && this.result?.user != null

    suspend fun logout() {
        BlindarFirebase.logout()
        preferencesRepository.clear()
    }

    companion object {
        private const val TAG = "RegisterManager"
    }

}