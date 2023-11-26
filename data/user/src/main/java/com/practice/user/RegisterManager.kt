package com.practice.user

import android.content.Intent
import android.util.Log
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseUser
import com.practice.firebase.BlindarFirebase
import com.practice.firebase.BlindarUserStatus
import javax.inject.Inject

class RegisterManager @Inject constructor() {
    suspend fun getUserState(): UserRegisterState {
        val blindarUser = BlindarFirebase.getBlindarUser()
        if (blindarUser is BlindarUserStatus.NotLoggedIn) {
            return UserRegisterState.NOT_LOGGED_IN
        }

        val user = blindarUser as BlindarUserStatus.LoginUser
        return getUserDataState(user.user)
    }

    private suspend fun getUserDataState(firebaseUser: FirebaseUser): UserRegisterState {
        return when {
            firebaseUser.displayName.isNullOrEmpty() -> UserRegisterState.USERNAME_MISSING
            BlindarFirebase.getschoolCode(firebaseUser.displayName!!) == null -> UserRegisterState.SCHOOL_NOT_SELECTED
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

            getUserDataState(user) == UserRegisterState.ALL_FILLED -> {
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

    companion object {
        private const val TAG = "RegisterManager"
    }

}