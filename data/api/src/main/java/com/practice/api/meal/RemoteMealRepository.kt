package com.practice.api.meal

import com.practice.api.meal.pojo.toMonthlyMeal
import com.practice.domain.meal.MonthlyMeal

class RemoteMealRepository(private val remote: RemoteMealDataSource) {
    suspend fun getMeals(schoolCode: Int, year: Int, month: Int): MonthlyMeal {
        return try {
            remote.getMeals(schoolCode, year, month).toMonthlyMeal(schoolCode, year, month)
        } catch (e: Exception) {
            e.printStackTrace()
            throw RemoteMealRepositoryException(e.message)
        }
    }
}

class RemoteMealRepositoryException(override val message: String?) : Exception(message)