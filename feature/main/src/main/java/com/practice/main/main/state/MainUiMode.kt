package com.practice.main.main.state

import com.practice.preferences.preferences.MainScreenMode

sealed interface MainUiMode {

    data object Loading : MainUiMode
    data object Calendar : MainUiMode
    data object Daily : MainUiMode

    fun MainUiMode.toMainScreenMode(): MainScreenMode? = when (this) {
        is Loading -> null
        is Calendar -> MainScreenMode.Calendar
        is Daily -> MainScreenMode.Daily
    }

    fun MainScreenMode.toUiLoadedMode(): MainUiMode = when (this) {
        MainScreenMode.Calendar -> Calendar
        MainScreenMode.Daily -> Daily
    }
}