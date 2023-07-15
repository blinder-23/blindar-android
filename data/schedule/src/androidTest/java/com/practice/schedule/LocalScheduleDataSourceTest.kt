package com.practice.schedule

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.practice.schedule.room.ScheduleDatabase
import com.practice.schedule.util.TestUtil
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
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
        assertThat(actual).containsExactlyInAnyOrderElementsOf(schedules)
    }

    @Test
    fun source_deleteSchedules() = runTest {
        val schedules = TestUtil.createScheduleEntity(5)
        source.insertSchedules(schedules)

        val deleted = schedules.subList(0, 2)
        val remain = schedules.subList(2, 5)
        source.deleteSchedules(deleted)

        val actual = source.getSchedules(schedules[0].year, schedules[0].month).first()
        assertThat(actual).containsExactlyInAnyOrderElementsOf(remain)
    }

    @Test
    fun source_deleteSchedules_byDate() = runTest {
        val schedules = TestUtil.createScheduleEntity(5)
        source.insertSchedules(schedules)
        source.deleteSchedules(schedules[0].year, schedules[0].month)

        val actual = source.getSchedules(schedules[0].year, schedules[0].month).first()
        assertThat(actual).isEmpty()
    }

    @Test
    fun source_clear() = runTest {
        val schedules = TestUtil.createScheduleEntity(5)
        source.insertSchedules(schedules)
        source.clear()

        val actual = source.getSchedules(schedules[0].year, schedules[0].month).first()
        assertThat(actual).isEmpty()
    }
}