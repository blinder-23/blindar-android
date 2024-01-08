package com.practice.main.state

import com.practice.preferences.preferences.MainScreenMode

enum class MainUiMode {
    LOADING,
    CALENDAR,
    DAILY,
}

fun MainScreenMode.toUiMode() = when (this) {
    MainScreenMode.Calendar -> MainUiMode.CALENDAR
    MainScreenMode.Daily -> MainUiMode.DAILY
}