package com.practice.main

import com.hsk.ktx.date.Date
import com.practice.domain.School
import com.practice.main.state.DailyData
import com.practice.main.state.MainUiMode
import com.practice.main.state.MainUiState
import com.practice.main.state.UiMeal
import com.practice.main.state.UiMeals
import com.practice.main.state.UiMemos
import com.practice.main.state.UiSchedules

internal fun previewMainUiState(): MainUiState {
    val now = Date.now()
    val (year, month, day) = now

    return MainUiState(
        userId = "",
        year = year,
        month = month,
        selectedDate = now,
        monthlyDataState = (1..3).map {
            DailyData(
                schoolCode = 1,
                date = now.plusDays(it),
                uiMeals = UiMeals(UiMeal(year, month, day, "", previewMenus, previewNutrients)),
                uiSchedules = UiSchedules(
                    date = now,
                    uiSchedules = previewSchedules,
                ),
                uiMemos = UiMemos(
                    date = now,
                    memos = previewMemos,
                ),
            )
        },
        selectedMealIndex = 0,
        isLoading = false,
        selectedSchool = School(
            name = "어떤 학교",
            schoolCode = -1,
        ),
        isNutrientPopupVisible = false,
        isMemoPopupVisible = false,
        mainUiMode = MainUiMode.CALENDAR,
    )
}