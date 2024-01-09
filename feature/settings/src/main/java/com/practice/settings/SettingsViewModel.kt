package com.practice.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practice.preferences.PreferencesRepository
import com.practice.preferences.preferences.MainScreenMode
import com.practice.settings.uistate.SettingsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository,
) : ViewModel() {

    val uiState: StateFlow<SettingsUiState> = preferencesRepository.userPreferencesFlow.map {
        SettingsUiState.SettingsUiStateImpl(
            mainScreenMode = it.mainScreenMode,
            isDailyAlarmEnabled = it.isDailyAlarmEnabled,
        )
    }.stateIn(viewModelScope, SharingStarted.Lazily, SettingsUiState.Loading)

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
}