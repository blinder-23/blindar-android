package com.example.server.schedule

import com.example.server.schedule.api.scheduleApi
import com.example.server.toEpochDate
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.Month

class RemoteScheduleRepositoryImplTest {

    private val dataSource: RemoteScheduleDataSource = RemoteScheduleDataSourceImpl(scheduleApi)

    @Test
    fun `test schedules`(): Unit = runBlocking {
        val year = 2022
        val month = 9
        val result = dataSource.getSchedules(year, month)

        assertThat(result.schedules).isNotEmpty
            .allSatisfy {
                val scheduleMonth = it.date.toEpochDate(9).month
                assertThat(scheduleMonth).isEqualTo(Month.SEPTEMBER)
            }
    }

}