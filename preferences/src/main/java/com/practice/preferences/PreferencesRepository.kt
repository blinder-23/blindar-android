package com.practice.preferences

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class PreferencesRepository @Inject constructor(private val dataStore: DataStore<Preferences>) {

    private val TAG = "PreferencesRepository"

    private object PreferenceKeys {
        val UI_MODE = stringPreferencesKey("ui-mode")
        val THEME_MODE = stringPreferencesKey("theme-mode")
        val FIRST_EXECUTION = booleanPreferencesKey("first-execution")
        val FETCHING_DATA = booleanPreferencesKey("fetching_data")
    }

    val userPreferencesFlow: Flow<UserPreferences> = dataStore.data.catch { exception ->
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

    suspend fun updateUiMode(uiMode: UiMode) {
        edit {
            it[PreferenceKeys.UI_MODE] = uiMode.name
        }
    }

    suspend fun updateThemeMode(themeMode: ThemeMode) {
        edit {
            it[PreferenceKeys.THEME_MODE] = themeMode.name
        }
    }

    suspend fun updateIsFirstExecution(isFirstExecution: Boolean) {
        edit {
            it[PreferenceKeys.FIRST_EXECUTION] = isFirstExecution
        }
    }

    suspend fun updateIsFetching(isFetching: Boolean) {
        edit {
            it[PreferenceKeys.FETCHING_DATA] = isFetching
        }
    }

    suspend fun clear() {
        edit {
            it.clear()
        }
    }

    private suspend fun edit(action: (MutablePreferences) -> Unit): Preferences {
        return dataStore.edit { action(it) }
    }

    /** Should only called once when **only** first preference object is needed.
     *  This function will cancel the collection of [userPreferencesFlow].
     */
    suspend fun fetchInitialPreferences() =
        mapUserPreferences(dataStore.data.first().toPreferences())


    private fun mapUserPreferences(preferences: Preferences): UserPreferences {
        val uiMode = UiMode.valueOf(
            value = preferences[PreferenceKeys.UI_MODE] ?: UiMode.Graphic.name
        )
        val themeMode = ThemeMode.valueOf(
            value = preferences[PreferenceKeys.THEME_MODE] ?: ThemeMode.SystemDefault.name
        )
        val isFirstExecution = preferences[PreferenceKeys.FIRST_EXECUTION] ?: true
        val isFetching = preferences[PreferenceKeys.FETCHING_DATA] ?: false
        return UserPreferences(uiMode, themeMode, isFirstExecution, isFetching)
    }

}