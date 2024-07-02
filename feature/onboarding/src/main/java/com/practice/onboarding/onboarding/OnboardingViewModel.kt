package com.practice.onboarding.onboarding

import android.content.Context
import android.content.Intent
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialResponse
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.auth.FirebaseUser
import com.practice.auth.RegisterManager
import com.practice.work.BlindarWorkManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val registerManager: RegisterManager,
) : ViewModel() {

    suspend fun tryGoogleLogin(
        context: Context,
        intent: Intent,
        onNewUserSignUp: (FirebaseUser) -> Unit,
        onExistingUserLogin: (FirebaseUser) -> Unit,
        onFail: () -> Unit,
    ) {
        registerManager.parseIntentAndSignInWithGoogle(
            intent = intent,
            onNewUserSignUp = onNewUserSignUp,
            onExistingUserLogin = {
                onExistingUserLogin(it)
                BlindarWorkManager.setOneTimeFetchDataWork(context)
                BlindarWorkManager.setFetchMemoFromServerWork(context, it.uid)
            },
            onFail = onFail
        )
    }

    /* 위에 있는 코드는 레거시 */

    fun parseIdToken(
        result: GetCredentialResponse,
        onNewUserSignUp: (FirebaseUser) -> Unit,
        onExistingUserLogin: (FirebaseUser) -> Unit,
        onFail: () -> Unit,
    ) {
        when (val credential = result.credential) {
            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        val googleIdTokenCredential =
                            GoogleIdTokenCredential.createFrom(credential.data)
                        signInWithGoogle(
                            idToken = googleIdTokenCredential.idToken,
                            onNewUserSignUp = onNewUserSignUp,
                            onExistingUserLogin = onExistingUserLogin,
                            onFail = onFail,
                        )
                    } catch (e: GoogleIdTokenParsingException) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    private fun signInWithGoogle(
        idToken: String,
        onNewUserSignUp: (FirebaseUser) -> Unit,
        onExistingUserLogin: (FirebaseUser) -> Unit,
        onFail: () -> Unit,
    ) {
        viewModelScope.launch {
            registerManager.signInWithGoogle(idToken, onNewUserSignUp, onExistingUserLogin, onFail)
        }
    }

    companion object {
        private const val TAG = "OnboardingViewModel"
    }

}