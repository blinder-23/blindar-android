package com.practice.schedule

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.practice.domain.schedule.Schedule
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
class ScheduleRepositoryTest {
    private lateinit var database: ScheduleDatabase
    private lateinit var source: LocalScheduleDataSource
    private lateinit var repository: ScheduleRepository

    private val schoolCode = TestUtil.schoolCode

    @Before
    fun init() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context,
            ScheduleDatabase::class.java
        ).build()
        source = LocalScheduleDataSource(database.scheduleDao())
        repository = ScheduleRepository(source)
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun repository_getSchedules_empty() = runTest {
        val actual = repository.getSchedules(schoolCode, 2022, 5).first()
        assertThat(actual).isEmpty()
    }

    @Test
    fun repository_insertSchedules() = runTest {
        val insertedSchedules = insertEntities(10)

        val actual = getSchedules(insertedSchedules[0])
        assertThat(actual).containsExactlyInAnyOrderElementsOf(insertedSchedules)
    }

    @Test
    fun repository_deleteSchedules_deleteAll() = runTest {
        val schedules = insertEntities(10)
        repository.deleteSchedules(*schedules.toTypedArray())

        val actual = getSchedules(schedules[0])
        assert(actual.isEmpty())
    }

    @Test
    fun repository_deleteSchedules_deleteOnlyPart() = runTest {
        val schedules = insertEntities(10)

        val deleted = schedules.subList(0, 2)
        val remain = schedules.subList(2, 10)
        repository.deleteSchedules(deleted)

        val actual = getSchedules(schedules[3])
        assertThat(actual).containsExactlyInAnyOrderElementsOf(remain)
    }

    @Test
    fun repository_deleteSchedules_byDate() = runTest {
        val schedules = insertEntities(10)
        repository.deleteSchedules(schoolCode, schedules[0].year, schedules[0].month)

        val actual = getSchedules(schedules[0])
        assertThat(actual).isEmpty()
    }

    @Test
    fun repository_clear() = runTest {
        val schedule = insertEntities(100).first()
        repository.clear()

        val actual = getSchedules(schedule)
        assertThat(actual).isEmpty()
    }

    private suspend fun insertEntities(count: Int): List<Schedule> {
        val schedules = TestUtil.createScheduleEntity(count)
        repository.insertSchedules(*schedules.toTypedArray())
        return schedules
    }

    private suspend fun getSchedules(schedule: Schedule): List<Schedule> {
        return repository.getSchedules(schoolCode, schedule.year, schedule.month).first()
    }
}