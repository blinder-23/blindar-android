package com.practice.hanbitlunch.api.meal

import com.practice.neisapi.NEISRetrofit
import kotlinx.coroutines.runBlocking
import org.junit.Test

class MealServiceDietInfoTest {

    @Test
    fun testApi() = runBlocking {
        val api = com.practice.neisapi.NEISRetrofit.mealApi
        val result = api.getMealOfMonth(
            officeCode = com.practice.neisapi.meal.seoulOfficeCode,
            schoolCode = com.practice.neisapi.meal.hanbitSchoolCode,
            mealYearMonth = "202205",
            pageSize = 2
        )
        println(result)
    }

}