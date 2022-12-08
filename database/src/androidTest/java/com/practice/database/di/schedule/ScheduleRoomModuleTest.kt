package com.practice.database.di.schedule

import com.practice.database.schedule.room.ScheduleDao
import com.practice.database.schedule.room.ScheduleDatabase
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class ScheduleRoomModuleTest {
    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var scheduleDatabase: ScheduleDatabase

    @Inject
    lateinit var scheduleDao: ScheduleDao

    @Test
    fun injectScheduleDatabase() {
        assertFalse(::scheduleDatabase.isInitialized)
        hiltRule.inject()
        assertTrue(::scheduleDatabase.isInitialized)
    }

    @Test
    fun injectScheduleDao() {
        assertFalse(::scheduleDao.isInitialized)
        hiltRule.inject()
        assertTrue(::scheduleDao.isInitialized)
    }
}