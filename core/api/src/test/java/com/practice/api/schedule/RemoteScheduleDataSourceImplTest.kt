package com.practice.api.schedule

import com.practice.api.schedule.api.scheduleApi
import com.practice.api.toEpochDate
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class RemoteScheduleRepositoryImplTest {

    private val dataSource: RemoteScheduleDataSource =
        RemoteScheduleDataSourceImpl(scheduleApi)

    @Test
    fun `test schedules`(): Unit = runBlocking {
        val year = 2022
        val month = 9
        val result = dataSource.getSchedules(year, month)

        assertThat(result.schedules).isNotEmpty
            .allSatisfy {
                val scheduleMonth = it.date.toEpochDate(9).month
                assertThat(scheduleMonth).isEqualTo(9)
            }
    }

}