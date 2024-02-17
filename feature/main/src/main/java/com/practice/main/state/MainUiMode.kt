package com.practice.main.state

import com.practice.preferences.preferences.MainScreenMode

enum class MainUiMode {
    LOADING,
    CALENDAR,
    DAILY;

    fun toMainScreenMode(): MainScreenMode? = when (this) {
        LOADING -> null
        CALENDAR -> MainScreenMode.Calendar
        DAILY -> MainScreenMode.Daily
    }
}

fun MainScreenMode.toUiMode() = when (this) {
    MainScreenMode.Calendar -> MainUiMode.CALENDAR
    MainScreenMode.Daily -> MainUiMode.DAILY
}