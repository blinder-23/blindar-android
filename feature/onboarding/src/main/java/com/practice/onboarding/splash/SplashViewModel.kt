package com.practice.onboarding.splash

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.practice.preferences.PreferencesRepository
import com.practice.user.RegisterManager
import com.practice.user.UserRegisterState
import com.practice.work.BlindarWorkManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository,
    private val registerManager: RegisterManager,
) : ViewModel() {
    suspend fun enqueueOneTimeWorkIfFirstExecution(context: Context) {
        val firstPreferences = preferencesRepository.userPreferencesFlow.first()
        if (firstPreferences.isFirstExecution) {
            BlindarWorkManager.setOneTimeFetchDataWork(context = context)
            preferencesRepository.updateIsFirstExecution(false)
            Log.d(TAG, "first work enqueued.")
        }
        BlindarWorkManager.setPeriodicFetchDataWork(context)
        Log.d(TAG, "Let's login!")
    }

    suspend fun getUserRegisterState(): UserRegisterState {
        return registerManager.getUserRegisterState()
    }

    fun onAutoLogin(context: Context) {
        uploadUserInfoOnAutoLogin(context)
        fetchDataFromRemoteOnAutoLogin(context)
    }

    private fun uploadUserInfoOnAutoLogin(context: Context) {
        BlindarWorkManager.setUserInfoToRemoteWork(context)
    }

    private fun fetchDataFromRemoteOnAutoLogin(context: Context) {
        BlindarWorkManager.setOneTimeFetchDataWork(context)
    }

    companion object {
        private val TAG = "SplashViewModel"
    }
}