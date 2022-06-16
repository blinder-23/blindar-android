package com.practice.neis.meal.pojo

import com.practice.neis.common.pojo.Header
import com.practice.neis.common.pojo.ResponseModel

data class MealResponseModel(
    override val header: Header,
    override val data: List<MealModel>
) : ResponseModel<MealModel>
