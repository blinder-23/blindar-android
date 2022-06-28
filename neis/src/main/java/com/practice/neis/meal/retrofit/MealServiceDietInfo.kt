package com.practice.neis.meal.retrofit

import com.practice.neis.common.NEISRetrofit
import com.practice.neis.common.apiKey
import com.practice.neis.common.hanbitSchoolCode
import com.practice.neis.common.seoulOfficeCode
import com.practice.neis.meal.pojo.MealResponseModel
import com.practice.neis.meal.util.MealDeserializer
import retrofit2.http.GET
import retrofit2.http.Query

interface MealServiceDietInfo {

    @GET("mealServiceDietInfo")
    suspend fun getMealOfMonth(
        @Query("KEY") key: String = apiKey,
        @Query("Type") type: String = "json",
        @Query("pIndex") pageIndex: Int = 1,
        @Query("pSize") pageSize: Int = 100,
        @Query("ATPT_OFCDC_SC_CODE") officeCode: String = seoulOfficeCode,
        @Query("SD_SCHUL_CODE") schoolCode: String = hanbitSchoolCode,
        @Query("MLSV_YMD") mealYearMonth: String,
    ): MealResponseModel
}

val NEISRetrofit.mealApi: MealServiceDietInfo
    get() = getRetrofit(
        responseType = MealResponseModel::class.java,
        typeAdapter = MealDeserializer()
    ).create(MealServiceDietInfo::class.java)