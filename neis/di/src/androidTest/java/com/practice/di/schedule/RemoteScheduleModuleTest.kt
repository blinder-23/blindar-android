package com.practice.di.schedule

import com.practice.neis.schedule.RemoteScheduleDataSource
import com.practice.neis.schedule.RemoteScheduleRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class RemoteScheduleModuleTest {
    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var dataSource: RemoteScheduleDataSource

    @Inject
    lateinit var repository: RemoteScheduleRepository

    @Test
    fun injectDataStore() {
        assertFalse(::dataSource.isInitialized)
        hiltRule.inject()
        assertTrue(::dataSource.isInitialized)
    }

    @Test
    fun injectRepository() {
        assertFalse(::repository.isInitialized)
        hiltRule.inject()
        assertTrue(::repository.isInitialized)
    }
}