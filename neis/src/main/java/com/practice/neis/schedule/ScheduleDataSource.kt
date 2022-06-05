package com.practice.neis.schedule

import com.practice.neis.common.NEISRetrofit
import com.practice.neis.common.getYearMonthString
import com.practice.neis.schedule.pojo.ScheduleModel
import com.practice.neis.schedule.retrofit.scheduleApi

class ScheduleDataSource {

    private val api = NEISRetrofit.scheduleApi

    suspend fun getSchedule(year: Int, month: Int): List<ScheduleModel> {
        val result = api.getScheduleOfMonth(scheduleYearMonth = getYearMonthString(year, month))

        val resultStatus = result.header.resultCode
        if (resultStatus.fail) {
            throw ScheduleDataSourceException(resultStatus.message)
        }
        return result.scheduleData
    }

}

internal class ScheduleDataSourceException(override val message: String) : Exception(message)