package com.practice.onboarding.splash

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.practice.firebase.BlindarFirebase
import com.practice.firebase.UserDataState
import com.practice.preferences.PreferencesRepository
import com.practice.work.BlindarWorkManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository,
) : ViewModel() {
    suspend fun enqueueOneTimeWorkIfFirstExecution(context: Context) {
        val firstPreferences = preferencesRepository.userPreferencesFlow.first()
        if (firstPreferences.isFirstExecution) {
            BlindarWorkManager.setOneTimeWork(context = context)
            preferencesRepository.updateIsFirstExecution(false)
            Log.d(TAG, "first work enqueued.")
        }
        BlindarWorkManager.setPeriodicWork(context)
        Log.d(TAG, "Let's login!")
    }

    suspend fun userDataState(): UserDataState {
        val user = FirebaseAuth.getInstance().currentUser
        Log.d(TAG, "current user: ${user?.uid}")
        return if (user == null) {
            UserDataState.NOT_LOGGED_IN
        } else {
            BlindarFirebase.getUserDataState(user)
        }
    }

    companion object {
        private val TAG = "SplashViewModel"
    }
}