package com.practice.settings.settings

import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practice.api.feedback.RemoteFeedbackRepository
import com.practice.api.feedback.repository.FeedbackResult
import com.practice.auth.RegisterManager
import com.practice.firebase.BlindarFirebase
import com.practice.firebase.BlindarUserStatus
import com.practice.preferences.PreferencesRepository
import com.practice.preferences.preferences.MainScreenMode
import com.practice.settings.settings.uistate.ProfileUiState
import com.practice.settings.settings.uistate.SettingsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository,
    private val feedbackRepository: RemoteFeedbackRepository,
    private val registerManager: RegisterManager,
) : ViewModel() {

    val uiState: StateFlow<SettingsUiState> = preferencesRepository.userPreferencesFlow.map {
        SettingsUiState.SettingsUiStateImpl(
            mainScreenMode = it.mainScreenMode,
            isDailyAlarmEnabled = it.isDailyAlarmEnabled,
            profileUiState = parseProfileUiState()
        )
    }.stateIn(viewModelScope, SharingStarted.Lazily, SettingsUiState.Loading)

    private val _logoutDialogOpen = MutableStateFlow(false)
    val logoutDialogOpen: StateFlow<Boolean> = _logoutDialogOpen.asStateFlow()

    private fun parseProfileUiState(): ProfileUiState {
        return when (val user = BlindarFirebase.getBlindarUser()) {
            is BlindarUserStatus.NotLoggedIn -> ProfileUiState(
                profileImageUri = null,
                username = "",
            )

            is BlindarUserStatus.LoginUser -> ProfileUiState(
                profileImageUri = user.user.photoUrl,
                username = user.user.displayName ?: "",
            )
        }
    }

    fun onLogoutDialogOpen() {
        _logoutDialogOpen.value = true
    }

    fun onLogoutDialogDismiss() {
        _logoutDialogOpen.value = false
    }

    fun onLogout() {
        _logoutDialogOpen.value = false
        registerManager.logout()
    }

    fun onToggleDailyMode(value: Boolean) {
        val newMainScreenMode = if (value) MainScreenMode.Daily else MainScreenMode.Calendar
        viewModelScope.launch {
            preferencesRepository.updateMainScreenMode(newMainScreenMode)
        }
    }

    fun onToggleDailyAlarm(value: Boolean) {
        viewModelScope.launch {
            preferencesRepository.updateDailyAlarmState(value)
        }
    }

    suspend fun sendFeedback(appVersionName: String, contents: String): Boolean {
        val user = BlindarFirebase.getBlindarUser()
        val userId = if (user is BlindarUserStatus.LoginUser) {
            user.user.uid
        } else {
            return false
        }
        val deviceName = Build.MODEL
        val osVersion = Build.VERSION.SDK_INT.toString()

        return feedbackRepository.sendFeedback(
            userId = userId,
            deviceName = deviceName,
            osVersion = osVersion,
            appVersion = appVersionName,
            contents = contents,
        ) is FeedbackResult.Success
    }
}