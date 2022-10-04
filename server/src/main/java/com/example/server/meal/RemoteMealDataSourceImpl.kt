package com.example.server.meal

import com.example.server.meal.api.MealApi
import com.example.server.meal.pojo.MealResponse

class RemoteMealDataSourceImpl(private val api: MealApi) : RemoteMealDataSource {
    override suspend fun getMeals(year: Int, month: Int): MealResponse {
        return api.getMeals(year, month)
    }
}