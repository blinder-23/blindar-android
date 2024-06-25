package com.practice.hanbitlunch.navigation

import com.practice.main.main.state.MainUiState
import com.practice.main.memo.MemoRoute
import com.practice.main.nutrient.NutrientRoute

val MainUiState.nutrientRoute: NutrientRoute
    get() = NutrientRoute(
        year = year,
        month = month,
        dayOfMonth = selectedDate.dayOfMonth,
        schoolCode = selectedSchoolCode,
        mealTime = selectedDateDataState.uiMeals[selectedMealIndex].mealTime,
    )

val MainUiState.memoRoute: MemoRoute
    get() = MemoRoute(
        year = year,
        month = month,
        dayOfMonth = selectedDate.dayOfMonth,
        schoolCode = selectedSchoolCode,
        userId = userId,
    )