package com.practice.api.meal

import android.util.Log
import com.practice.api.meal.pojo.toMonthlyMeal
import com.practice.domain.meal.MonthlyMeal

class RemoteMealRepository(private val remote: RemoteMealDataSource) {
    suspend fun getMeals(schoolCode: Int, year: Int, month: Int): MonthlyMeal {
        return try {
            remote.getMeals(schoolCode, year, month).let {
                Log.d("RemoteMealRepository", "meal $year $month: ${it.response.size}")
                it.toMonthlyMeal(schoolCode, year, month)
            }
        } catch (e: Exception) {
            throw RemoteMealRepositoryException(e.message)
        }
    }
}

class RemoteMealRepositoryException(override val message: String?) : Exception(message)