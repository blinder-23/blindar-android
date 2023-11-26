package com.practice.onboarding.splash

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.practice.preferences.PreferencesRepository
import com.practice.user.RegisterStateManager
import com.practice.user.UserRegisterState
import com.practice.work.BlindarWorkManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository,
    private val registerStateManager: RegisterStateManager,
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

    suspend fun userDataState(): UserRegisterState {
        return registerStateManager.getUserState()
    }

    // TODO: uploadToRemote(가칭) 함수 만들고, server와 firebase에 각각 업로드하는 함수 호출
    fun uploadUserInfoToFirebaseOnAutoLogin(context: Context) {
        BlindarWorkManager.setUserInfoToFirebaseWork(context)
    }

    companion object {
        private val TAG = "SplashViewModel"
    }
}