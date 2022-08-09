package com.practice.neis.schedule

import com.hsk.ktx.getDateString
import com.practice.neis.schedule.pojo.ScheduleModel
import com.practice.neis.schedule.retrofit.ScheduleApi

class ScheduleRemoteDataSourceImpl(private val api: ScheduleApi) : ScheduleRemoteDataSource {

    override suspend fun getSchedules(year: Int, month: Int): List<ScheduleModel> {
        val result = api.getScheduleOfMonth(scheduleYearMonth = getDateString(year, month))

        val resultStatus = result.header.resultCode
        if (resultStatus.fail) {
            throw ScheduleRemoteDataSourceException(resultStatus.message)
        }
        return result.data
    }

}

internal class ScheduleRemoteDataSourceException(override val message: String) : Exception(message)