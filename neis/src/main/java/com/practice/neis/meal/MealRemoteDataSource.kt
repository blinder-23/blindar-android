package com.practice.neis.meal

import com.hsk.ktx.getDateString
import com.practice.neis.meal.pojo.MealModel
import com.practice.neis.meal.retrofit.MealServiceDietInfo

class MealRemoteDataSource(private val api: MealServiceDietInfo) {

    suspend fun getMeal(year: Int, month: Int): List<MealModel> {
        val result = api.getMealOfMonth(mealYearMonth = getDateString(year, month))

        val resultStatus = result.header.resultCode
        if (resultStatus.fail) {
            throw MealDataSourceException(resultStatus.message)
        }
        return result.data
    }
}

internal class MealDataSourceException(override val message: String = "") : Exception(message)