package com.practice.neis.meal

import com.practice.neis.meal.pojo.MealModel
import com.practice.neis.meal.pojo.MealResultCode
import com.practice.neis.meal.retrofit.NEISRetrofit
import com.practice.neis.meal.retrofit.hanbitSchoolCode
import com.practice.neis.meal.retrofit.seoulOfficeCode

class MealDataSource {

    private val retrofit = NEISRetrofit.mealApi

    suspend fun getMeal(year: Int, month: Int): List<MealModel> {
        val result = retrofit.getMealOfMonth(
            officeCode = seoulOfficeCode,
            schoolCode = hanbitSchoolCode,
            mealYearMonth = getYearMonthString(year, month),
        )

        val resultStatus = result.header.resultCode
        if (resultStatus.code != MealResultCode.successCode) {
            println(resultStatus)
            throw MealDataSourceException(resultStatus.message)
        }
        return result.mealData
    }

    private fun getYearMonthString(year: Int, month: Int): String {
        val formattedMonth = month.toString().padStart(2, '0') // 01, 02, ...
        return "$year$formattedMonth"
    }
}

class MealDataSourceException(override val message: String = "") : Exception(message)