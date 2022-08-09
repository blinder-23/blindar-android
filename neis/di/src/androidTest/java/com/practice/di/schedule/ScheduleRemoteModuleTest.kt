package com.practice.di.schedule

import com.practice.neis.schedule.ScheduleRemoteDataSource
import com.practice.neis.schedule.ScheduleRemoteDataSourceImpl
import com.practice.neis.schedule.ScheduleRemoteRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class ScheduleRemoteModuleTest {
    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var dataSource: ScheduleRemoteDataSource

    @Inject
    lateinit var repository: ScheduleRemoteRepository

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