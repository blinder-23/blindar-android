package com.practice.main.main.state

import com.hsk.ktx.date.Date
import com.practice.designsystem.calendar.core.YearMonth
import com.practice.domain.School
import com.practice.main.state.UiMeals
import com.practice.main.state.UiMemos
import com.practice.main.state.UiSchedules

data class MainUiState(
    val userId: String,
    val selectedDate: Date,
    val monthlyDataState: List<DailyData>,
    val selectedMealIndex: Int,
    val isLoading: Boolean,
    val selectedSchool: School,
    val isMealDialogVisible: Boolean,
    val isScheduleDialogVisible: Boolean,
    val mainUiMode: MainUiMode,
) {
    val year: Int
        get() = selectedDate.year
    val month: Int
        get() = selectedDate.month
    val yearMonth: YearMonth
        get() = YearMonth(year, month)

    val selectedSchoolCode: Int
        get() = selectedSchool.schoolCode

    val selectedDateDataState: DailyData
        get() = monthlyDataState.firstOrNull { it.date == selectedDate }
            ?: DailyData.Empty

    val isMealExists: Boolean
        get() = !selectedDateDataState.uiMeals.isEmpty

    val isScheduleOrMemoExists: Boolean
        get() = selectedDateDataState.memoDialogElements.isNotEmpty()

    companion object {
        val EMPTY = MainUiState(
            userId = "",
            selectedDate = Date.now(),
            monthlyDataState = emptyList(),
            selectedMealIndex = 0,
            isLoading = false,
            selectedSchool = School.EmptySchool,
            isMealDialogVisible = false,
            isScheduleDialogVisible = false,
            mainUiMode = MainUiMode.Loading,
        )
    }
}