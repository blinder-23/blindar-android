package com.practice.preferences

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlin.coroutines.CoroutineContext

interface PreferencesRepository : CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = SupervisorJob() + Dispatchers.Default

    val userPreferencesFlow: Flow<UserPreferences>

    suspend fun updateUiMode(uiMode: UiMode)
    suspend fun updateThemeMode(themeMode: ThemeMode)
    suspend fun updateIsFirstExecution(isFirstExecution: Boolean)
    suspend fun updateScreenMode(screenMode: ScreenMode)
    suspend fun increaseRunningWorkCount()
    suspend fun decreaseRunningWorkCount()
    suspend fun updateSelectedSchool(schoolId: Int, schoolName: String)
    suspend fun clear()
    suspend fun fetchInitialPreferences(): UserPreferences
}