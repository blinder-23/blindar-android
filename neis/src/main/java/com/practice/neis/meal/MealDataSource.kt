package com.practice.neis.meal

import com.practice.neis.common.getYearMonthString
import com.practice.neis.common.hanbitSchoolCode
import com.practice.neis.common.seoulOfficeCode
import com.practice.neis.meal.pojo.MealModel
import com.practice.neis.meal.retrofit.NEISRetrofit

class MealDataSource {

    private val retrofit = NEISRetrofit.mealApi

    suspend fun getMeal(year: Int, month: Int): List<MealModel> {
        val result = retrofit.getMealOfMonth(
            officeCode = seoulOfficeCode,
            schoolCode = hanbitSchoolCode,
            mealYearMonth = getYearMonthString(year, month),
        )

        val resultStatus = result.header.resultCode
        if (resultStatus.fail) {
            throw MealDataSourceException(resultStatus.message)
        }
        return result.mealData
    }
}

class MealDataSourceException(override val message: String = "") : Exception(message)