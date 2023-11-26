package com.practice.user

import android.app.Activity
import android.content.Intent
import android.util.Log
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.practice.firebase.BlindarFirebase
import com.practice.firebase.BlindarUserStatus
import com.practice.preferences.PreferencesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

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
        Log.d(TAG, "username: $username, school empty: ${preferences.isSchoolCodeEmpty}")
        return when {
            username != null && !preferences.isSchoolCodeEmpty -> UserRegisterState.ALL_FILLED
            username.isNullOrEmpty() -> UserRegisterState.USERNAME_MISSING
            BlindarFirebase.getSchoolCode(firebaseUser.displayName!!) == null -> UserRegisterState.SCHOOL_NOT_SELECTED
            else -> UserRegisterState.ALL_FILLED
        }
    }

    // TODO: BlindarFirebase가 담당하고 있던 로그인, 가입 전환 로직을 전부 이쪽으로 옮기자

    suspend fun parseIntentAndSignInWithGoogle(
        intent: Intent,
        onNewUserSignUp: (FirebaseUser) -> Unit,
        onExistingUserLogin: (FirebaseUser) -> Unit,
        onFail: () -> Unit,
    ) {
        try {
            val firebaseUser = BlindarFirebase.signInWithGoogle(intent)
            checkGoogleRegisterStatus(
                user = firebaseUser,
                onNewUserSignUp = onNewUserSignUp,
                onExistingUserLogin = onExistingUserLogin,
                onFail = onFail,
            )
        } catch (e: ApiException) {
            Log.d(TAG, "parse account fail", e)
            onFail()
        }
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
                Log.e(TAG, "sign in with google fail")
                onFail()
            }

            getUserRegisterState(user) == UserRegisterState.ALL_FILLED -> {
                // existing user login
                Log.d(TAG, "user ${user.uid} re-login")
                onExistingUserLogin(user)
            }

            else -> {
                // new user registers
                BlindarFirebase.storeUsername(user.displayName!!)?.addOnSuccessListener {
                    onNewUserSignUp(user)
                }?.addOnFailureListener {
                    Log.e(TAG, "username owner set failed: ${user.uid}, ${user.displayName}", it)
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
            Log.d(TAG, "phone auth code verification complete")
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
            catchVerificationFailException(e, onVerificationFail)
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken,
        ) {
            Log.d(TAG, "phone code is sent.")
            onCodeSent(verificationId, token)
        }

        override fun onCodeAutoRetrievalTimeOut(p0: String) {
            // TODO: UI에서 남은 초 보여주기?
            Log.e(TAG, "phone auth code timeout! $p0")
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
                    Log.d(TAG, "sign in with phone credential successful!")
                    onSignInSuccess()
                } else if (task.exception is FirebaseAuthInvalidCredentialsException) {
                    Log.e(TAG, "sign in with phone credential fail: invalid verification code")
                    onSignInFail()
                } else {
                    Log.e(TAG, "sign in with phone credential fail: unknown error", task.exception)
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
            Log.d(TAG, "user register state: $state")
            when (state) {
                UserRegisterState.NOT_LOGGED_IN -> onNewUserSignUp()
                UserRegisterState.USERNAME_MISSING -> onUsernameNotSet()
                UserRegisterState.SCHOOL_NOT_SELECTED -> onSchoolNotSelected()
                UserRegisterState.ALL_FILLED -> {
                    // TODO: 다시 로그인하면 학교 ID가 보이지 않는 문제 해결
                    val schoolCode = BlindarFirebase.getschoolCode()
//                    preferencesRepository.updateSelectedSchool()
                    onExistingUserLogin()
                }
            }
        }
    }

    private fun catchVerificationFailException(
        e: FirebaseException,
        onFail: () -> Unit,
    ) {
        when (e) {
            is FirebaseAuthInvalidCredentialsException -> {
                Log.e(TAG, "phone verification fail: invalid request")
            }

            is FirebaseTooManyRequestsException -> {
                Log.e(TAG, "phone verification fail: firebase auth quota exceeded. try later.")
            }

            is FirebaseAuthMissingActivityForRecaptchaException -> {
                Log.e(TAG, "phone verification fail: reCAPTCHA is attempted with null activity")
            }

            else -> {
                Log.e(TAG, "phone verification fail: unknown error")
            }
        }
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

    companion object {
        private const val TAG = "RegisterManager"
    }

}