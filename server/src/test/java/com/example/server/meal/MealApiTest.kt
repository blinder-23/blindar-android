package com.example.server.meal

import com.example.server.meal.api.mealApi
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

class MealApiTest {

    private val api = mealApi

    @Test
    fun testMealApi() = runBlocking {
        val response = api.getMeals(2022, 10)
        println(response.response.map { it.ymd })
    }
}