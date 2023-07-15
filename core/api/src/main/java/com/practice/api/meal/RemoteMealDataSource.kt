package com.practice.api.meal

import com.practice.api.meal.pojo.MealResponse

interface RemoteMealDataSource {
    suspend fun getMeals(year: Int, month: Int): MealResponse
}