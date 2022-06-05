package com.practice.neis.schedule.retrofit

import com.practice.neis.common.NEISRetrofit
import com.practice.neis.common.apiKey
import com.practice.neis.schedule.pojo.ScheduleResponseModel
import com.practice.neis.schedule.util.ScheduleDeserializer
import retrofit2.http.GET
import retrofit2.http.Query

interface ScheduleInfo {

    @GET("SchoolSchedule")
    suspend fun getScheduleOfMonth(
        @Query("KEY") key: String = apiKey,
        @Query("Type") type: String = "json",
        @Query("pIndex") pageIndex: Int = 1,
        @Query("pSize") pageSize: Int = 100,
        @Query("ATPT_OFCDC_SC_CODE") officeCode: String,
        @Query("SD_SCHUL_CODE") schoolCode: String,
        @Query("AA_YMD") scheduleYearMonth: String,
    ): ScheduleResponseModel
}

val NEISRetrofit.scheduleApi: ScheduleInfo
    get() = getRetrofit(
        responseType = ScheduleResponseModel::class.java,
        typeAdapter = ScheduleDeserializer()
    ).create(ScheduleInfo::class.java)