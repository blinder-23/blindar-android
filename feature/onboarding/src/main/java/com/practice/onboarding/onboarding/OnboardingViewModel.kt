package com.practice.onboarding.onboarding

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.practice.auth.RegisterManager
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

    companion object {
        private const val TAG = "OnboardingViewModel"
    }

}