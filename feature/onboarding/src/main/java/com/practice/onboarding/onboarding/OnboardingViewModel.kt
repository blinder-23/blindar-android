package com.practice.onboarding.onboarding

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialCancellationException
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

    suspend fun getCredential(
        context: Context,
        credentialManager: CredentialManager,
        request: GetCredentialRequest,
    ): GetCredentialResponse? {
        return try {
            credentialManager.getCredential(
                context = context,
                request = request,
            )
        } catch (e: GetCredentialCancellationException) {
            e.printStackTrace()
            null
        }
    }

    fun parseIdToken(
        result: GetCredentialResponse,
        onNewUserSignUp: (FirebaseUser) -> Unit,
        onExistingUserLogin: (FirebaseUser) -> Unit,
        onFail: () -> Unit,
        context: Context,
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
                            context = context,
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
        context: Context,
    ) {
        viewModelScope.launch {
            registerManager.signInWithGoogle(
                idToken = idToken,
                onNewUserSignUp = onNewUserSignUp,
                onExistingUserLogin = { user ->
                    onExistingUserLogin(user)
                    setFetchRemoteDataWork(context, user.uid)
                },
                onFail = onFail,
            )
        }
    }

    private fun setFetchRemoteDataWork(context: Context, userId: String) {
        Log.d(TAG, "set work for $userId")
        BlindarWorkManager.setOneTimeFetchDataWork(context)
        BlindarWorkManager.setFetchMemoFromServerWork(context, userId)
    }

    companion object {
        private const val TAG = "OnboardingViewModel"
    }

}