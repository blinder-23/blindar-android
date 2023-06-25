package com.practice.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

private const val TEST_DATASTORE_NAME = "test-datastore"

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class PreferencesRepositoryImplTest {

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher + Job())
    private val testContext = ApplicationProvider.getApplicationContext<Context>()
    private val testDataStore: DataStore<Preferences> = PreferenceDataStoreFactory.create(
        scope = testScope,
        produceFile = { testContext.preferencesDataStoreFile(TEST_DATASTORE_NAME) }
    )
    private val preferences = PreferencesRepositoryImpl(testDataStore)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testScope.runTest { preferences.clear() }
        testDispatcher.cancel()
    }

    private fun preferences(
        uiMode: UiMode = UiMode.Graphic,
        themeMode: ThemeMode = ThemeMode.SystemDefault,
        screenMode: ScreenMode = ScreenMode.Default,
    ): UserPreferences = UserPreferences(uiMode, themeMode, screenMode)

    @Test
    fun repository_fetchInitialPreferences() = runTest {
        val initialPreferences = preferences.fetchInitialPreferences()
        assertThat(initialPreferences.uiMode).isEqualTo(UiMode.Graphic)
        assertThat(initialPreferences.themeMode).isEqualTo(ThemeMode.SystemDefault)
    }

    @Test
    fun repository_updateUiMode() = runTest {
        update {
            updateUiMode(UiMode.ScreenReader)
            updateUiMode(UiMode.Graphic)
            updateUiMode(UiMode.ScreenReader)
        }
        val result = preferences.dropFirstAndTake(3)

        assertThat(result[0]).isEqualTo(preferences(UiMode.ScreenReader))
        assertThat(result[1]).isEqualTo(preferences(UiMode.Graphic))
        assertThat(result[2]).isEqualTo(preferences(UiMode.ScreenReader))
    }

    @Test
    fun repository_updateThemeMode() = runTest {
        update {
            updateThemeMode(ThemeMode.Dark)
            updateThemeMode(ThemeMode.Light)
        }
        val result = preferences.dropFirstAndTake(2)

        assertThat(result[0]).isEqualTo(preferences(themeMode = ThemeMode.Dark))
        assertThat(result[1]).isEqualTo(preferences(themeMode = ThemeMode.Light))
    }

    @Test
    fun repository_updateBoth() = runTest {
        update {
            updateThemeMode(ThemeMode.Light)
            updateUiMode(UiMode.ScreenReader)
        }
        val result = preferences.dropFirstAndTake(2)

        assertThat(result[0]).isEqualTo(preferences(UiMode.Graphic, ThemeMode.Light))
        assertThat(result[1]).isEqualTo(preferences(UiMode.ScreenReader, ThemeMode.Light))
    }

    @Test
    fun repository_updateFirstExecution() = runTest {
        update {
            updateIsFirstExecution(true)
        }

        val isFirstExecution = preferences.dropFirstAndTake(1).first().isFirstExecution
        assertThat(isFirstExecution).isTrue
    }

    @Test
    fun repository_initialSchoolIdIsEmpty() = runTest {
        val isSchoolIdEmpty = preferences.fetchInitialPreferences().isSchoolIdEmpty
        assert(isSchoolIdEmpty)
    }

    @Test
    fun repository_updateSchoolId() = runTest {
        val schoolId = 1234567
        update {
            updateSchoolId(schoolId)
        }

        preferences.dropFirstAndTake(1).first().apply {
            assert(!isSchoolIdEmpty)
            assertThat(this.schoolId).isEqualTo(schoolId)
        }
    }

    private suspend fun PreferencesRepositoryImpl.dropFirstAndTake(take: Int) =
        userPreferencesFlow.drop(1).take(take).toList()

    private fun CoroutineScope.update(block: suspend PreferencesRepository.() -> Unit) =
        launch {
            block(preferences)
        }
}