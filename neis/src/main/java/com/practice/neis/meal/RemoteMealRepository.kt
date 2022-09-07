package com.practice.neis.meal

import com.practice.neis.meal.pojo.MealModel

class RemoteMealRepository(private val remoteMealDataSource: RemoteMealDataSource) {

    suspend fun getMealData(year: Int, month: Int): List<MealModel> {
        return try {
            remoteMealDataSource.getMeals(year, month)
        } catch (e: MealDataSourceException) {
            throw RemoteMealRepositoryException(e.message)
        }
    }

}

class RemoteMealRepositoryException(override val message: String) : Exception(message)