package com.practice.neis.schedule.retrofit

import com.practice.neis.common.NeisApi
import com.practice.neis.common.hanbitSchoolCode
import com.practice.neis.common.seoulOfficeCode
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ScheduleApiTest {

    @Test
    fun getScheduleOfMonth() = runTest {
        val api = NeisApi.scheduleApi
        val result = api.getScheduleOfMonth(
            officeCode = seoulOfficeCode,
            schoolCode = hanbitSchoolCode,
            scheduleYearMonth = "202205",
            pageSize = 1
        )
        println(result)
    }

}