package com.practice.di.schedule

import com.practice.database.schedule.ScheduleLocalDataSource
import com.practice.database.schedule.ScheduleRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class ScheduleModuleTest {
    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var scheduleLocalDataSource: ScheduleLocalDataSource

    @Inject
    lateinit var scheduleRepository: ScheduleRepository

    @Test
    fun injectScheduleLocalDataSource() {
        assertFalse(::scheduleLocalDataSource.isInitialized)
        hiltRule.inject()
        assertTrue(::scheduleLocalDataSource.isInitialized)
    }

    @Test
    fun injectScheduleRepository() {
        assertFalse(::scheduleRepository.isInitialized)
        hiltRule.inject()
        assertTrue(::scheduleRepository.isInitialized)
    }
}