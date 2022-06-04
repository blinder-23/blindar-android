package com.practice.neis.meal

import com.practice.neis.meal.pojo.MealModel

class MealRepository(private val mealDataSource: MealDataSource = MealDataSource()) {

    suspend fun getMealData(year: Int, month: Int): List<MealModel> {
        return mealDataSource.getMeal(year, month)
    }

}