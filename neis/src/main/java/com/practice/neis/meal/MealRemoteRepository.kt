package com.practice.neis.meal

import com.practice.neis.meal.pojo.MealModel

class MealRemoteRepository(private val mealRemoteDataSource: MealRemoteDataSource = MealRemoteDataSource()) {

    suspend fun getMealData(year: Int, month: Int): List<MealModel> {
        return try {
            mealRemoteDataSource.getMeal(year, month)
        } catch (e: MealDataSourceException) {
            throw MealRemoteRepositoryException(e.message)
        }
    }

}

class MealRemoteRepositoryException(override val message: String) : Exception(message)