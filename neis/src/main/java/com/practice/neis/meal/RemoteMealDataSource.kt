package com.practice.neis.meal

import com.practice.neis.meal.pojo.MealModel

sealed interface RemoteMealDataSource {
    suspend fun getMeals(year: Int, month: Int): List<MealModel>
}