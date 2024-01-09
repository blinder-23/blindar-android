package com.practice.main.state

import com.practice.preferences.preferences.MainScreenMode

enum class MainUiMode {
    LOADING,
    NOT_SET,
    CALENDAR,
    DAILY,
}

fun MainScreenMode.toUiMode() = when (this) {
    MainScreenMode.NOT_SELECTED -> MainUiMode.NOT_SET
    MainScreenMode.Calendar -> MainUiMode.CALENDAR
    MainScreenMode.Daily -> MainUiMode.DAILY
}