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

enum class ScreenMode {
    Default,
    List,
}

data class UserPreferences(
    val uiMode: UiMode,
    val themeMode: ThemeMode,
    val screenMode: ScreenMode,
    val isFirstExecution: Boolean = true,
    val runningWorksCount: Int = 0,
)