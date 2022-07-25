package com.practice.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class DataStoreModuleTest {
    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var dataStore: DataStore<Preferences>

    @Test
    fun injectPreferencesDataStore() {
        assertThat(::dataStore.isInitialized).isFalse
        hiltRule.inject()
        assertThat(::dataStore.isInitialized).isTrue
    }
}