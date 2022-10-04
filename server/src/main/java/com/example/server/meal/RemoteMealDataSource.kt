package com.example.server.meal

import com.example.server.meal.pojo.MealResponse

interface RemoteMealDataSource {
    suspend fun getMeals(year: Int, month: Int): MealResponse
}