package com.practice.main.main

import com.hsk.ktx.date.Date
import com.practice.domain.School
import com.practice.main.main.state.DailyData
import com.practice.main.main.state.MainUiMode
import com.practice.main.main.state.MainUiState
import com.practice.main.main.state.UiMeal
import com.practice.main.main.state.UiMeals
import com.practice.main.main.state.UiMemos
import com.practice.main.main.state.UiSchedules

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
                uiMeals = UiMeals(UiMeal(year, month, day, "", previewUiMenus, previewUiNutrients)),
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
        isNutrientDialogVisible = false,
        isMemoDialogVisible = false,
        isMealDialogVisible = false,
        isScheduleDialogVisible = false,
        mainUiMode = MainUiMode.CALENDAR,
    )
}