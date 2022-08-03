package com.practice.neis.meal

import com.hsk.ktx.getDateString
import com.practice.neis.common.seoulOfficeCode
import com.practice.neis.meal.pojo.MealModel
import com.practice.neis.meal.pojo.MenuModel
import com.practice.neis.meal.pojo.NutrientModel
import com.practice.neis.meal.pojo.OriginModel

class MealFakeRemoteDataSource : MealRemoteDataSource {
    private val mealModels = (1..20).map {
        MealModel(
            officeCode = seoulOfficeCode,
            officeName = "테스트교육청",
            schoolCode = 999,
            schoolName = "테스트학교",
            mealCode = 1,
            mealName = "점심",
            date = getDateString(2022, 8, it),
            numberStudents = 1,
            menu = listOf(MenuModel(name = "메뉴 $it", allergic = emptyList())),
            originCountries = listOf(OriginModel(ingredient = "재료 $it", originCountry = "테스트나라")),
            calorie = (100 * it).toDouble(),
            nutrients = listOf(NutrientModel(name = "테스트영양소", unit = "g", amount = it.toDouble())),
            fromDate = getDateString(2022, 8, it),
            endDate = getDateString(2022, 8, it)
        )
    }.toSet()

    override suspend fun getMeals(year: Int, month: Int): List<MealModel> {
        return mealModels.filter {
            it.date.contains("$year${month.toString().padStart(2, '0')}")
        }
    }
}