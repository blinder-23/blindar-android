package com.practice.neis.schedule

import com.practice.neis.common.NEISRetrofit
import com.practice.neis.common.getYearMonthString
import com.practice.neis.schedule.pojo.ScheduleModel
import com.practice.neis.schedule.retrofit.scheduleApi

class ScheduleRemoteDataSource {

    private val api = NEISRetrofit.scheduleApi

    suspend fun getSchedule(year: Int, month: Int): List<ScheduleModel> {
        val result = api.getScheduleOfMonth(scheduleYearMonth = getYearMonthString(year, month))

        val resultStatus = result.header.resultCode
        if (resultStatus.fail) {
            throw ScheduleRemoteDataSourceException(resultStatus.message)
        }
        return result.data
    }

}

internal class ScheduleRemoteDataSourceException(override val message: String) : Exception(message)