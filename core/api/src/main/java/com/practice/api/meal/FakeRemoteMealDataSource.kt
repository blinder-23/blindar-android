package com.practice.api.meal

import com.practice.api.meal.pojo.MealModel
import com.practice.api.meal.pojo.MealResponse
import com.practice.api.meal.pojo.MenuModel

class FakeRemoteMealDataSource : RemoteMealDataSource {
    private val mealModels = (1..20).map {
        val day = it.toString().padStart(2, '0')
        MealModel(
            ymd = "202210${day}",
            dishes = listOf(MenuModel(menu = "menu $it", allergies = listOf(it))),
            origins = emptyList(),
            nutrients = emptyList()
        )
    }.toMutableList()

    override suspend fun getMeals(year: Int, month: Int): MealResponse {
        val matches = mealModels.filter {
            val mealYear = it.ymd.substring(0..3).toInt()
            val mealMonth = it.ymd.substring(4..5).toInt()
            mealYear == year && mealMonth == month
        }
        return MealResponse(matches)
    }
}