package com.practice.neis.meal

import com.practice.neis.meal.pojo.MealModel

sealed interface MealRemoteDataSource {
    suspend fun getMeals(year: Int, month: Int): List<MealModel>
}