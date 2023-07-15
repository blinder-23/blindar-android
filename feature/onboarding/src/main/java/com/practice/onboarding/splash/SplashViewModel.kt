package com.practice.onboarding.splash

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
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
        Log.d("SplashViewModel", "Let's login!")
    }

    companion object {
        private val TAG = "SplashViewModel"
    }
}