package com.practice.main.state

import com.practice.preferences.preferences.MainScreenMode

enum class MainUiMode {
    LOADING,
    NOT_SET,
    CALENDAR,
    DAILY;

    fun toMainScreenMode(): MainScreenMode? = when (this) {
        LOADING -> null
        NOT_SET -> MainScreenMode.NOT_SELECTED
        CALENDAR -> MainScreenMode.Calendar
        DAILY -> MainScreenMode.Daily
    }
}

fun MainScreenMode.toUiMode() = when (this) {
    MainScreenMode.NOT_SELECTED -> MainUiMode.NOT_SET
    MainScreenMode.Calendar -> MainUiMode.CALENDAR
    MainScreenMode.Daily -> MainUiMode.DAILY
}