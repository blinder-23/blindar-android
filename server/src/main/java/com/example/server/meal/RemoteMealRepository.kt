package com.example.server.meal

import com.example.server.meal.pojo.MealResponse

class RemoteMealRepository(private val remote: RemoteMealDataSource) {
    suspend fun getMeals(year: Int, month: Int): MealResponse {
        return try {
            remote.getMeals(year, month)
        } catch (e: Exception) {
            throw RemoteMealRepositoryException(e.message)
        }
    }
}

class RemoteMealRepositoryException(override val message: String?) : Exception(message)