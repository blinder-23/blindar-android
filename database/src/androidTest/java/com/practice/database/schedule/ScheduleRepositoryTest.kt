package com.practice.database.schedule

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.practice.database.TestUtil
import com.practice.database.schedule.entity.ScheduleEntity
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
class ScheduleRepositoryTest {
    private lateinit var database: ScheduleDatabase
    private lateinit var source: LocalScheduleDataSource
    private lateinit var repository: ScheduleRepository

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
        val actual = repository.getSchedules(2022, 5)
        assert(actual.isEmpty())
    }

    @Test
    fun repository_insertSchedules() = runTest {
        val schedules = insertEntities(10)
        val actual = getSchedules(schedules[0])

        assertEquals(schedules, actual)
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
        repository.deleteSchedules(schedules.subList(0, 2))

        val actual = getSchedules(schedules[3])
        assertEquals(schedules.subList(2, 10), actual)
    }

    @Test
    fun repository_deleteSchedules_byDate() = runTest {
        val schedules = insertEntities(10)
        repository.deleteSchedules(schedules[0].year, schedules[0].month)

        val actual = getSchedules(schedules[0])
        assert(actual.isEmpty())
    }

    @Test
    fun repository_clear() = runTest {
        val schedule = insertEntities(100).first()
        repository.clear()

        val actual = getSchedules(schedule)
        assert(actual.isEmpty())
    }

    private suspend fun insertEntities(count: Int): List<ScheduleEntity> {
        val schedules = TestUtil.createScheduleEntity(count)
        repository.insertSchedules(*schedules.toTypedArray())
        return schedules
    }

    private suspend fun getSchedules(schedule: ScheduleEntity): List<ScheduleEntity> {
        return repository.getSchedules(schedule.year, schedule.month)
    }
}