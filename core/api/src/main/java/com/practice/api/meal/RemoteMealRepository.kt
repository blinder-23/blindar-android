package com.practice.api.meal

import android.util.Log
import com.practice.api.meal.pojo.MealResponse

class RemoteMealRepository(private val remote: RemoteMealDataSource) {
    suspend fun getMeals(year: Int, month: Int): MealResponse {
        return try {
            remote.getMeals(year, month).apply {
                Log.d("RemoteMealRepository", "meal $year $month: ${response.size}")
            }
        } catch (e: Exception) {
            throw RemoteMealRepositoryException(e.message)
        }
    }
}

class RemoteMealRepositoryException(override val message: String?) : Exception(message)