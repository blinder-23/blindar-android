package com.practice.main.nutrient

import com.hsk.ktx.date.Date
import com.practice.main.state.UiNutrient

sealed interface NutrientUiState {
    val date: Date
    val mealTime: String

    data class Loading(
        override val date: Date,
        override val mealTime: String,
    ) : NutrientUiState

    data class Fail(
        override val date: Date,
        override val mealTime: String,
    ) : NutrientUiState

    data class Success(
        override val date: Date,
        override val mealTime: String,
        val nutrients: List<UiNutrient>,
    ) : NutrientUiState
}