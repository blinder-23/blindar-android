package com.practice.database.schedule

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.practice.database.TestUtil
import com.practice.database.schedule.room.ScheduleDao
import com.practice.database.schedule.room.ScheduleDatabase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class ScheduleDaoTest {
    private lateinit var database: ScheduleDatabase
    private lateinit var dao: ScheduleDao

    @Before
    fun init() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context,
            ScheduleDatabase::class.java
        ).build()
        dao = database.scheduleDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun dao_insertSchedules() = runTest {
        val schedules = TestUtil.createScheduleEntityRoom(5)
        dao.insertSchedules(schedules)

        val actual = dao.getSchedule(schedules[0].date.slice(0..5))
        assertEquals(schedules, actual)
    }

    @Test
    fun dao_deleteSchedules() = runTest {
        val schedules = TestUtil.createScheduleEntityRoom(5)
        dao.insertSchedules(schedules)
        dao.deleteSchedules(schedules.subList(0, 2))

        val actual = dao.getSchedule(schedules[0].date.slice(0..5))
        assertEquals(schedules.subList(2, 5), actual)
    }

    @Test
    fun dao_deleteSchedules_byDate() = runTest {
        val schedules = TestUtil.createScheduleEntityRoom(5)
        val date = schedules[0].date.slice(0..5)
        dao.insertSchedules(schedules)
        dao.deleteSchedules(date)

        val actual = dao.getSchedule(date)
        assert(actual.isEmpty())
    }

    @Test
    fun dao_clear() = runTest {
        val schedules = TestUtil.createScheduleEntityRoom(5)
        dao.insertSchedules(schedules)
        dao.clear()

        val actual = dao.getSchedule(schedules[0].date.slice(0..5))
        assert(actual.isEmpty())
    }


}