package com.example.server.schedule

import com.example.server.schedule.api.scheduleApi
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.Month

class RemoteScheduleRepositoryImplTest {

    private val repository: RemoteScheduleDataSource = RemoteScheduleDataSourceImpl(scheduleApi)

    @Test
    fun `test schedules`(): Unit = runBlocking {
        val year = 2022
        val month = 9
        val result = repository.getSchedules(year, month)

        assertThat(result.schedules).isNotEmpty
            .allSatisfy { LocalDate.ofEpochDay(9).month == Month.SEPTEMBER }
    }

}