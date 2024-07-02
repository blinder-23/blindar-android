package com.practice.settings.settings.uistate

import com.practice.preferences.preferences.MainScreenMode

sealed class SettingsUiState {
    data object Loading : SettingsUiState()
    data class SettingsUiStateImpl(
        val mainScreenMode: MainScreenMode,
        val isDailyAlarmEnabled: Boolean,
        val profileUiState: ProfileUiState,
    ) : SettingsUiState()
}