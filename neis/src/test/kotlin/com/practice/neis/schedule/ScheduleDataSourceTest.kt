package com.practice.neis.schedule

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ScheduleDataSourceTest {

    private val source = ScheduleDataSource()

    @Test
    fun getSchedule() = runTest {
        val result = source.getSchedule(2022, 5)
        println(result)
    }

}