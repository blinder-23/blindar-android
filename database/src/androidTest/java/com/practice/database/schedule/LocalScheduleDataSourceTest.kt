package com.practice.database.schedule

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.practice.database.TestUtil
import com.practice.database.schedule.room.ScheduleDatabase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class LocalScheduleDataSourceTest {
    private lateinit var database: ScheduleDatabase
    private lateinit var source: LocalScheduleDataSource

    @Before
    fun init() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context,
            ScheduleDatabase::class.java
        ).build()
        source = LocalScheduleDataSource(database.scheduleDao())
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun source_getSchedules() = runTest {
        val schedules = TestUtil.createScheduleEntity(5)
        source.insertSchedules(schedules)

        val actual = source.getSchedules(schedules[0].year, schedules[0].month).first()
        assertEquals(schedules, actual)
    }

    @Test
    fun source_deleteSchedules() = runTest {
        val schedules = TestUtil.createScheduleEntity(5)
        source.insertSchedules(schedules)
        source.deleteSchedules(schedules.subList(0, 2))

        val actual = source.getSchedules(schedules[0].year, schedules[0].month).first()
        assertEquals(schedules.subList(2, 5), actual)
    }

    @Test
    fun source_deleteSchedules_byDate() = runTest {
        val schedules = TestUtil.createScheduleEntity(5)
        source.insertSchedules(schedules)
        source.deleteSchedules(schedules[0].year, schedules[0].month)

        val actual = source.getSchedules(schedules[0].year, schedules[0].month).first()
        assert(actual.isEmpty())
    }

    @Test
    fun source_clear() = runTest {
        val schedules = TestUtil.createScheduleEntity(5)
        source.insertSchedules(schedules)
        source.clear()

        val actual = source.getSchedules(schedules[0].year, schedules[0].month).first()
        assert(actual.isEmpty())
    }
}