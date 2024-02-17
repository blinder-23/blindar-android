package com.practice.preferences

import com.practice.preferences.preferences.MainScreenMode
import com.practice.preferences.preferences.ThemeMode
import kotlinx.coroutines.flow.MutableStateFlow

class FakePreferencesRepository : PreferencesRepository {

    private val initialPreferences = UserPreferences(
        mainScreenMode = MainScreenMode.Daily,
        themeMode = ThemeMode.SystemDefault,
        isFirstExecution = true,
        runningWorksCount = 0,
        schoolCode = emptySchoolCode,
        schoolName = emptySchoolName,
        memoIdCounter = 0,
        isDailyAlarmEnabled = false,
    )
    private var preferences: UserPreferences = initialPreferences

    override val userPreferencesFlow = MutableStateFlow(preferences)

    override suspend fun updateMainScreenMode(mainScreenMode: MainScreenMode) {
        preferences = preferences.copy(mainScreenMode = mainScreenMode)
        emitNewValue()
    }

    override suspend fun updateThemeMode(themeMode: ThemeMode) {
        preferences = preferences.copy(themeMode = themeMode)
        emitNewValue()
    }

    override suspend fun updateIsFirstExecution(isFirstExecution: Boolean) {
        preferences = preferences.copy(isFirstExecution = isFirstExecution)
        emitNewValue()
    }

    override suspend fun increaseRunningWorkCount() {
        val count = preferences.runningWorksCount
        preferences = preferences.copy(runningWorksCount = count + 1)
        emitNewValue()
    }

    override suspend fun decreaseRunningWorkCount() {
        val count = preferences.runningWorksCount
        preferences = preferences.copy(runningWorksCount = count - 1)
        emitNewValue()
    }

    override suspend fun updateSelectedSchool(schoolCode: Int, schoolName: String) {
        preferences = preferences.copy(
            schoolCode = schoolCode,
            schoolName = schoolName
        )
    }

    override suspend fun getAndIncreaseMemoIdCount(): Int {
        val counter = preferences.memoIdCounter
        preferences = preferences.copy(
            memoIdCounter = counter + 1
        )
        emitNewValue()
        return counter
    }

    override suspend fun updateDailyAlarmState(isEnabled: Boolean) {
        preferences = preferences.copy(
            isDailyAlarmEnabled = isEnabled
        )
    }

    override suspend fun clear() {
        preferences = initialPreferences
        emitNewValue()
    }

    override suspend fun fetchInitialPreferences(): UserPreferences {
        return preferences
    }

    private suspend fun emitNewValue() {
        userPreferencesFlow.emit(preferences)
    }
}