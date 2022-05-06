package com.practice.hanbitlunch.api.meal

import com.practice.hanbitlunch.api.NEISRetrofit
import kotlinx.coroutines.runBlocking
import org.junit.Test

class MealServiceDietInfoTest {

    @Test
    fun testApi() = runBlocking {
        val api = NEISRetrofit.mealApi
        val result = api.getMealOfMonth(
            officeCode = seoulOfficeCode,
            schoolCode = hanbitSchoolCode,
            mealYearMonth = "202205",
            pageSize = 2
        )
        println(result)
    }

}