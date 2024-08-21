package com.practice.onboarding

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.auth.FirebaseUser
import com.practice.api.feedback.FeedbackRequest
import com.practice.api.feedback.RemoteFeedbackRepository
import com.practice.auth.RegisterManager
import com.practice.firebase.BlindarFirebase
import com.practice.firebase.BlindarUserStatus
import com.practice.work.BlindarWorkManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val registerManager: RegisterManager,
    private val feedbackRepository: RemoteFeedbackRepository,
) : ViewModel() {

    // TODO: Android Studio가 Kotlin 2.0 Frontend를 지원하면 주석 해제하기
//    val isErrorDialogVisible: StateFlow<Boolean>
//        field = MutableStateFlow(false)
    private val _loginException = MutableStateFlow<Exception?>(null)
    val loginException: StateFlow<Exception?> = _loginException.asStateFlow()

    private val _sendLogEnabled = MutableStateFlow(true)
    val sendLogEnabled: StateFlow<Boolean> = _sendLogEnabled.asStateFlow()

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
        } catch (e: Exception) {
            showErrorDialog(e)
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
                        showErrorDialog(e)
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

    private fun showErrorDialog(e: Exception) {
        _loginException.value = e
    }

    fun hideErrorDialog() {
        _loginException.value = null
        _sendLogEnabled.value = true
    }

    fun sendFeedback(appVersionName: String) {
        viewModelScope.launch {
            _sendLogEnabled.value = false
            feedbackRepository.sendFeedback(
                FeedbackRequest(
                    userId = getUserId(),
                    feedback = loginException.value.toFeedbackMessage(),
                    appVersionName = appVersionName,
                )
            )
            hideErrorDialog()
        }
    }

    private fun Exception?.toFeedbackMessage() = this?.let { exception ->
        """
        Login error log
        Error message: ${exception.localizedMessage}
        Stack trace:
        ${exception.stackTraceToString()}
        """
            .trimIndent()
    } ?: "Default login error message"

    private fun getUserId(): String {
        return when (val user = BlindarFirebase.getBlindarUser()) {
            is BlindarUserStatus.LoginUser -> user.user.uid
            else -> ""
        }
    }

    companion object {
        private const val TAG = "OnboardingViewModel"
    }

}