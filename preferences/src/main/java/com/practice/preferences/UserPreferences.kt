package com.practice.preferences

enum class UiMode {
    Graphic,
    ScreenReader,
}

enum class ThemeMode {
    Light,
    Dark,
    SystemDefault,
}

data class UserPreferences(
    val uiMode: UiMode,
    val themeMode: ThemeMode,
    val isFirstExecution: Boolean = true,
    val isFetching: Boolean = false,
)