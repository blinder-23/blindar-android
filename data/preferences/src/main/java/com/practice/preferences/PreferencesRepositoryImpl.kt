package com.practice.preferences

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.practice.preferences.preferences.MainScreenMode
import com.practice.preferences.preferences.ThemeMode
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
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
        //        val UI_MODE = stringPreferencesKey("ui-mode")
        val MAIN_SCREEN_MODE = stringPreferencesKey("main-screen-mode")
        val THEME_MODE = stringPreferencesKey("theme-mode")
        val FIRST_EXECUTION = booleanPreferencesKey("first-execution")
        val RUNNING_WORKS_COUNT = intPreferencesKey("running-works-count")
        val SCHOOL_CODE = intPreferencesKey("school-id")
        val SCHOOL_NAME = stringPreferencesKey("school-name")
        val MEMO_ID_COUNTER = intPreferencesKey("memo-id-counter")
        val NOTIFICATION_ENABLED = booleanPreferencesKey("daily-notification-enabled")
    }

    override val userPreferencesFlow: StateFlow<UserPreferences> =
        dataStore.data.catch { exception ->
            when (exception) {
                is IOException -> {
                    emit(emptyPreferences())
                }

                else -> throw exception
            }
        }.map { preferences ->
            mapUserPreferences(preferences)
        }.stateIn(this, SharingStarted.Eagerly, UserPreferences.emptyPreferences)

    override suspend fun updateMainScreenMode(mainScreenMode: MainScreenMode) {
        edit {
            it[PreferenceKeys.MAIN_SCREEN_MODE] = mainScreenMode.name
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

    override suspend fun getAndIncreaseMemoIdCount(): Int {
        val currentCount = userPreferencesFlow.value.memoIdCounter
        edit {
            it[PreferenceKeys.MEMO_ID_COUNTER] = currentCount + 1
        }
        return currentCount
    }

    override suspend fun updateDailyAlarmState(isEnabled: Boolean) {
        edit {
            it[PreferenceKeys.NOTIFICATION_ENABLED] = isEnabled
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
        val mainScreenMode = MainScreenMode.valueOf(
            value = preferences[PreferenceKeys.MAIN_SCREEN_MODE] ?: MainScreenMode.Daily.name
        )
        val themeMode = ThemeMode.valueOf(
            value = preferences[PreferenceKeys.THEME_MODE] ?: ThemeMode.SystemDefault.name
        )
        val isFirstExecution = preferences[PreferenceKeys.FIRST_EXECUTION] ?: true
        val runningWorksCount = preferences[PreferenceKeys.RUNNING_WORKS_COUNT] ?: 0
        val schoolCode = preferences[PreferenceKeys.SCHOOL_CODE] ?: emptySchoolCode
        val schoolName = preferences[PreferenceKeys.SCHOOL_NAME] ?: emptySchoolName
        val memoIdCounter = preferences[PreferenceKeys.MEMO_ID_COUNTER] ?: 0
        val isDailyNotificationEnabled = preferences[PreferenceKeys.NOTIFICATION_ENABLED] ?: false
        return UserPreferences(
            mainScreenMode = mainScreenMode,
            themeMode = themeMode,
            isFirstExecution = isFirstExecution,
            runningWorksCount = runningWorksCount,
            schoolCode = schoolCode,
            schoolName = schoolName,
            memoIdCounter = memoIdCounter,
            isDailyAlarmEnabled = isDailyNotificationEnabled,
        )
    }

}