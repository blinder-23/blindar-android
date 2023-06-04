package com.practice.schedule

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.practice.schedule.room.ScheduleDao
import com.practice.schedule.room.ScheduleDatabase
import com.practice.schedule.room.yearMonth
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

        val actual = dao.getSchedule(schedules[0].yearMonth).first()
        assertThat(actual).containsExactlyInAnyOrderElementsOf(schedules)
    }

    @Test
    fun dao_deleteSchedules() = runTest {
        val schedules = TestUtil.createScheduleEntityRoom(5)
        dao.insertSchedules(schedules)

        val deleted = schedules.subList(0, 2)
        val remain = schedules.subList(2, 5)
        dao.deleteSchedules(deleted)

        val actual = dao.getSchedule(schedules[0].yearMonth).first()
        assertThat(actual).containsExactlyInAnyOrderElementsOf(remain)
    }

    @Test
    fun dao_deleteSchedules_byDate() = runTest {
        val schedules = TestUtil.createScheduleEntityRoom(5)
        val date = schedules[0].yearMonth
        dao.insertSchedules(schedules)
        dao.deleteSchedules(date)

        val actual = dao.getSchedule(date).first()
        assertThat(actual).isEmpty()
    }

    @Test
    fun dao_clear() = runTest {
        val schedules = TestUtil.createScheduleEntityRoom(5)
        dao.insertSchedules(schedules)
        dao.clear()

        val actual = dao.getSchedule(schedules[0].yearMonth).first()
        assertThat(actual).isEmpty()
    }


}