package com.practice.onboarding.onboarding

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.practice.user.RegisterManager
import com.practice.work.BlindarWorkManager
import dagger.hilt.android.lifecycle.HiltViewModel
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
        Log.d(TAG, "google login launcher success: ${intent.data}")
        registerManager.parseIntentAndSignInWithGoogle(
            intent = intent,
            onNewUserSignUp = onNewUserSignUp,
            onExistingUserLogin = {
                onExistingUserLogin(it)
                BlindarWorkManager.setOneTimeWork(context)
                BlindarWorkManager.setFetchMemoFromServerWork(context, it.uid)
            },
            onFail = onFail
        )
    }

    fun onGoogleLoginLauncherFail(result: ActivityResult) {
        Log.e(TAG, "google login launcher fail: ${result.resultCode}")
    }

    companion object {
        private const val TAG = "OnboardingViewModel"
    }

}