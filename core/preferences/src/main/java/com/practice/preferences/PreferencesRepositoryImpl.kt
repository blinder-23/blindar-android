package com.practice.preferences

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

class PreferencesRepositoryImpl @Inject constructor(private val dataStore: DataStore<Preferences>) :
    PreferencesRepository {

    private val requestUpdateWorkCounts = Channel<Int>()

    init {
        consumeUpdateWorkCountRequests()
    }

    private val TAG = "PreferencesRepository"

    private object PreferenceKeys {
        val UI_MODE = stringPreferencesKey("ui-mode")
        val THEME_MODE = stringPreferencesKey("theme-mode")
        val SCREEN_MODE = stringPreferencesKey("screen-mode")
        val FIRST_EXECUTION = booleanPreferencesKey("first-execution")
        val RUNNING_WORKS_COUNT = intPreferencesKey("running-works-count")
        val SCHOOL_CODE = intPreferencesKey("school-id")
        val SCHOOL_NAME = stringPreferencesKey("school-name")
    }

    override val userPreferencesFlow: Flow<UserPreferences> = dataStore.data.catch { exception ->
        when (exception) {
            is IOException -> {
                Log.e(TAG, "Error while reading preferences.", exception)
                emit(emptyPreferences())
            }

            else -> throw exception
        }
    }.map { preferences ->
        mapUserPreferences(preferences)
    }

    override suspend fun updateUiMode(uiMode: UiMode) {
        edit {
            it[PreferenceKeys.UI_MODE] = uiMode.name
        }
    }

    override suspend fun updateThemeMode(themeMode: ThemeMode) {
        edit {
            it[PreferenceKeys.THEME_MODE] = themeMode.name
        }
    }

    override suspend fun updateIsFirstExecution(isFirstExecution: Boolean) {
        edit {
            it[PreferenceKeys.FIRST_EXECUTION] = isFirstExecution
        }
    }

    override suspend fun updateScreenMode(screenMode: ScreenMode) {
        edit {
            it[PreferenceKeys.SCREEN_MODE] = screenMode.name
        }
    }

    override suspend fun increaseRunningWorkCount() {
        requestUpdateWorkCounts.send(1)
    }

    override suspend fun decreaseRunningWorkCount() {
        requestUpdateWorkCounts.send(-1)
    }

    private fun consumeUpdateWorkCountRequests() = launch {
        for (diff in requestUpdateWorkCounts) {
            updateRunningWorkCount(diff)
        }
    }

    private suspend fun updateRunningWorkCount(diff: Int) {
        edit {
            val currentCount = it[PreferenceKeys.RUNNING_WORKS_COUNT] ?: 0
            it[PreferenceKeys.RUNNING_WORKS_COUNT] = currentCount + diff
        }
    }

    override suspend fun updateSelectedSchool(schoolCode: Int, schoolName: String) {
        edit {
            it[PreferenceKeys.SCHOOL_CODE] = schoolCode
            it[PreferenceKeys.SCHOOL_NAME] = schoolName
        }
    }

    override suspend fun clear() {
        edit {
            it.clear()
        }
    }

    private suspend fun edit(action: (MutablePreferences) -> Unit) {
        dataStore.edit {
            action(it)
        }
    }

    /**
     * Should be called when a single preference object is needed.
     * This function doesn't cancel the collection of [dataStore].
     */
    override suspend fun fetchInitialPreferences() =
        mapUserPreferences(dataStore.data.first().toPreferences())


    private fun mapUserPreferences(preferences: Preferences): UserPreferences {
        val uiMode = UiMode.valueOf(
            value = preferences[PreferenceKeys.UI_MODE] ?: UiMode.Graphic.name
        )
        val themeMode = ThemeMode.valueOf(
            value = preferences[PreferenceKeys.THEME_MODE] ?: ThemeMode.SystemDefault.name
        )
        val screenMode = ScreenMode.valueOf(
            value = preferences[PreferenceKeys.SCREEN_MODE] ?: ScreenMode.Default.name
        )
        val isFirstExecution = preferences[PreferenceKeys.FIRST_EXECUTION] ?: true
        val runningWorksCount = preferences[PreferenceKeys.RUNNING_WORKS_COUNT] ?: 0
        val schoolCode = preferences[PreferenceKeys.SCHOOL_CODE] ?: emptySchoolCode
        val schoolName = preferences[PreferenceKeys.SCHOOL_NAME] ?: emptySchoolName
        return UserPreferences(
            uiMode = uiMode,
            themeMode = themeMode,
            screenMode = screenMode,
            isFirstExecution = isFirstExecution,
            runningWorksCount = runningWorksCount,
            schoolCode = schoolCode,
            schoolName = schoolName,
        )
    }

}