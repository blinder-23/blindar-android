package com.practice.splash

import android.content.Context
import androidx.lifecycle.ViewModel
import com.practice.auth.RegisterManager
import com.practice.auth.UserRegisterState
import com.practice.work.BlindarWorkManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val registerManager: RegisterManager,
) : ViewModel() {

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