package com.practice.neis.meal

import com.practice.neis.common.NEISRetrofit
import com.practice.neis.common.getYearMonthString
import com.practice.neis.meal.pojo.MealModel
import com.practice.neis.meal.retrofit.mealApi

class MealDataSource {

    private val retrofit = NEISRetrofit.mealApi

    suspend fun getMeal(year: Int, month: Int): List<MealModel> {
        val result = retrofit.getMealOfMonth(mealYearMonth = getYearMonthString(year, month))

        val resultStatus = result.header.resultCode
        if (resultStatus.fail) {
            throw MealDataSourceException(resultStatus.message)
        }
        return result.data
    }
}

internal class MealDataSourceException(override val message: String = "") : Exception(message)