package com.practice.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.practice.preferences.preferences.MainScreenMode
import com.practice.preferences.preferences.ThemeMode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
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

    @Test
    fun repository_fetchInitialPreferences() = runTest {
        val initialPreferences = preferences.fetchInitialPreferences()
        assertThat(initialPreferences.mainScreenMode).isEqualTo(MainScreenMode.Calendar)
        assertThat(initialPreferences.themeMode).isEqualTo(ThemeMode.SystemDefault)
    }

    @Test
    fun repository_updateUiMode() = runTest {
        update {
            updateMainScreenMode(MainScreenMode.Daily)
            updateMainScreenMode(MainScreenMode.Calendar)
            updateMainScreenMode(MainScreenMode.Daily)
        }
        val result = preferences.dropFirstAndTake(3)

        assertThat(result[0].mainScreenMode).isEqualTo(MainScreenMode.Daily)
        assertThat(result[1].mainScreenMode).isEqualTo(MainScreenMode.Calendar)
        assertThat(result[2].mainScreenMode).isEqualTo(MainScreenMode.Daily)
    }

    @Test
    fun repository_updateThemeMode() = runTest {
        update {
            updateThemeMode(ThemeMode.Dark)
            updateThemeMode(ThemeMode.Light)
        }
        val result = preferences.dropFirstAndTake(2)

        assertThat(result[0].themeMode).isEqualTo(ThemeMode.Dark)
        assertThat(result[1].themeMode).isEqualTo(ThemeMode.Light)
    }

    @Test
    fun repository_updateBoth() = runTest {
        update {
            updateThemeMode(ThemeMode.Light)
            updateMainScreenMode(MainScreenMode.Daily)
        }
        val result = preferences.dropFirstAndTake(2)

        assertThat(result[0].themeMode).isEqualTo(ThemeMode.Light)
        assertThat(result[1].mainScreenMode).isEqualTo(MainScreenMode.Daily)
        assertThat(result[1].themeMode).isEqualTo(ThemeMode.Light)
    }

    @Test
    fun repository_updateFirstExecution() = runTest {
        update {
            updateIsFirstExecution(true)
        }

        val isFirstExecution = preferences.take(1).first().isFirstExecution
        assertThat(isFirstExecution).isTrue
    }

    @Test
    fun repository_initialSchoolCodeIsEmpty() = runTest {
        val isSchoolCodeEmpty = preferences.fetchInitialPreferences().isSchoolCodeEmpty
        assert(isSchoolCodeEmpty)
    }

    @Test
    fun repository_updateSchoolCode() = runTest {
        val schoolCode = 1234567
        val schoolName = "testSchool"
        update {
            updateSelectedSchool(schoolCode, schoolName)
        }

        preferences.dropFirstAndTake(1).first().apply {
            assert(!isSchoolCodeEmpty)
            assertThat(this.schoolCode).isEqualTo(schoolCode)
        }
    }

    private suspend fun PreferencesRepositoryImpl.take(take: Int) =
        userPreferencesFlow.take(take).toList()

    private suspend fun PreferencesRepositoryImpl.dropFirstAndTake(take: Int) =
        userPreferencesFlow.drop(1).take(take).toList()

    private fun CoroutineScope.update(block: suspend PreferencesRepository.() -> Unit) =
        launch {
            block(preferences)
        }
}