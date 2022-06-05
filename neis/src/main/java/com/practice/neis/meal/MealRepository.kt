package com.practice.neis.meal

import com.practice.neis.meal.pojo.MealModel

class MealRepository(private val mealDataSource: MealDataSource = MealDataSource()) {

    suspend fun getMealData(year: Int, month: Int): List<MealModel> {
        return try {
            mealDataSource.getMeal(year, month)
        } catch (e: MealDataSourceException) {
            throw MealRepositoryException(e.message)
        }
    }

}

class MealRepositoryException(override val message: String) : Exception(message)