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
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

private const val TEST_DATASTORE_NAME = "test-datastore"

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class PreferencesRepositoryTest {

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher + Job())
    private val testContext = ApplicationProvider.getApplicationContext<Context>()
    private val testDataStore: DataStore<Preferences> = PreferenceDataStoreFactory.create(
        scope = testScope,
        produceFile = { testContext.preferencesDataStoreFile(TEST_DATASTORE_NAME) }
    )
    private val testRepository = PreferencesRepository(testDataStore)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testScope.runTest { testRepository.clear() }
        testDispatcher.cancel()
    }

    @Test
    fun repository_fetchInitialPreferences() = runTest {
        val initialPreferences = testRepository.fetchInitialPreferences()
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
        val result = testRepository.dropFirstAndTakeToList(3)

        assertThat(result[0]).isEqualTo(
            UserPreferences(UiMode.ScreenReader, ThemeMode.SystemDefault)
        )
        assertThat(result[1]).isEqualTo(
            UserPreferences(UiMode.Graphic, ThemeMode.SystemDefault)
        )
        assertThat(result[2]).isEqualTo(
            UserPreferences(UiMode.ScreenReader, ThemeMode.SystemDefault)
        )
    }

    @Test
    fun repository_updateThemeMode() = runTest {
        update {
            updateThemeMode(ThemeMode.Dark)
            updateThemeMode(ThemeMode.Light)
        }
        val result = testRepository.dropFirstAndTakeToList(2)

        assertThat(result[0]).isEqualTo(
            UserPreferences(UiMode.Graphic, ThemeMode.Dark)
        )
        assertThat(result[1]).isEqualTo(
            UserPreferences(UiMode.Graphic, ThemeMode.Light)
        )
    }

    @Test
    fun repository_updateBoth() = runTest {
        update {
            updateThemeMode(ThemeMode.Light)
            updateUiMode(UiMode.ScreenReader)
        }
        val result = testRepository.dropFirstAndTakeToList(2)

        assertThat(result[0]).isEqualTo(
            UserPreferences(UiMode.Graphic, ThemeMode.Light)
        )
        assertThat(result[1]).isEqualTo(
            UserPreferences(UiMode.ScreenReader, ThemeMode.Light)
        )
    }

    @Test
    fun repository_updateFirstExecution() = runTest {
        update {
            updateIsFirstExecution(true)
        }

        val isFirstExecution = testRepository.dropFirstAndTakeToList(1).first().isFirstExecution
        assertThat(isFirstExecution).isTrue()
    }

    private suspend fun PreferencesRepository.dropFirstAndTakeToList(take: Int) =
        userPreferencesFlow.drop(1).take(take).toList()

    private fun CoroutineScope.update(block: suspend PreferencesRepository.() -> Unit) =
        launch {
            block(testRepository)
        }
}