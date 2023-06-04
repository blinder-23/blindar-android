package com.practice.api.meal

import com.practice.api.meal.api.mealApi
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

class MealApiTest {

    private val api = mealApi

    @Test
    fun testMealApi() = runBlocking {
        val response = api.getMeals(2022, 10)
        println(response.response.map { it.ymd }.all {
            it.substring(0..3) == "2022" && it.substring(4..5) == "10"
        })
    }
}