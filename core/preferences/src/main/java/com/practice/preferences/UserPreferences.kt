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
    val schoolCode: Int = emptySchoolCode,
    val schoolName: String = emptySchoolName,
    val memoIdCounter: Int = 0,
) {
    val isSchoolCodeEmpty: Boolean
        get() = schoolCode == emptySchoolCode

    companion object {
        val emptyPreferences = UserPreferences(
            uiMode = UiMode.Graphic,
            themeMode = ThemeMode.SystemDefault,
            screenMode = ScreenMode.Default,
        )
    }
}

internal const val emptySchoolCode = -1
internal const val emptySchoolName = ""