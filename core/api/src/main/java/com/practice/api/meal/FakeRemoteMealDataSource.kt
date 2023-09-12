package com.practice.api.meal

import com.hsk.ktx.date.Date
import com.practice.api.meal.pojo.MealModel
import com.practice.api.meal.pojo.MealResponse
import com.practice.api.meal.pojo.MenuModel

class FakeRemoteMealDataSource : RemoteMealDataSource {
    private val mealModels = (1..20).map {
        val year = Date.now().year
        val month = Date.now().month.toString().padStart(2, '0')
        val day = it.toString().padStart(2, '0')
        MealModel(
            ymd = "${year}${month}${day}",
            dishes = listOf(MenuModel(menu = "menu $it", allergies = listOf(it))),
            origins = emptyList(),
            nutrients = emptyList()
        )
    }.toMutableList()

    override suspend fun getMeals(schoolCode: Int, year: Int, month: Int): MealResponse {
        val matches = mealModels.filterIndexed { index, mealModel ->
            val mealYear = mealModel.ymd.substring(0..3).toInt()
            val mealMonth = mealModel.ymd.substring(4..5).toInt()
            val mealDay = mealModel.ymd.substring(6..7).toInt()
            // fake 객체만 day와 schoolCode를 연결
            mealDay == schoolCode && mealYear == year && mealMonth == month
        }
        return MealResponse(matches)
    }
}