package com.practice.neis.meal.pojo

import com.practice.neis.common.pojo.Header

data class MealResponseModel(
    val header: Header,
    val mealData: List<MealModel>
)
