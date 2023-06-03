package com.practice.api.meal

import com.practice.api.meal.api.MealApi
import com.practice.api.meal.pojo.MealResponse

class RemoteMealDataSourceImpl(private val api: MealApi) : RemoteMealDataSource {
    override suspend fun getMeals(year: Int, month: Int): MealResponse {
        return api.getMeals(year, month).apply {
//            Log.d("RemoteMealDataSourceImpl", "meal $year $month: ${response.map { it.ymd }}")
        }
    }
}