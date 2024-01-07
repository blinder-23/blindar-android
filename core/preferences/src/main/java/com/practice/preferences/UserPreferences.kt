package com.practice.preferences

import com.practice.preferences.preferences.MainScreenMode
import com.practice.preferences.preferences.ScreenMode
import com.practice.preferences.preferences.ThemeMode

data class UserPreferences(
    val mainScreenMode: MainScreenMode,
    val themeMode: ThemeMode,
    val screenMode: ScreenMode,
    val isFirstExecution: Boolean,
    val runningWorksCount: Int,
    val schoolCode: Int,
    val schoolName: String,
    val memoIdCounter: Int,
    val isDailyAlarmEnabled: Boolean,
) {
    val isSchoolCodeEmpty: Boolean
        get() = schoolCode == emptySchoolCode

    val isSchoolNameEmpty: Boolean
        get() = schoolName == emptySchoolName

    companion object {
        val emptyPreferences = UserPreferences(
            mainScreenMode = MainScreenMode.Calendar,
            themeMode = ThemeMode.SystemDefault,
            screenMode = ScreenMode.Default,
            isFirstExecution = true,
            runningWorksCount = 0,
            schoolCode = emptySchoolCode,
            schoolName = emptySchoolName,
            memoIdCounter = 1,
            isDailyAlarmEnabled = false,
        )
    }
}

internal const val emptySchoolCode = -1
internal const val emptySchoolName = ""