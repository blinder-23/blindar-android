package com.practice.main.main.state

import com.hsk.ktx.date.Date
import com.practice.designsystem.calendar.core.YearMonth
import com.practice.domain.School

data class MainUiState(
    val userId: String,
    val year: Int,
    val month: Int,
    val selectedDate: Date,
    val monthlyDataState: List<DailyData>,
    val selectedMealIndex: Int,
    val isLoading: Boolean,
    val selectedSchool: School,
    val isMealDialogVisible: Boolean,
    val isScheduleDialogVisible: Boolean,
    val mainUiMode: MainUiMode,
) {
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
            year = Date.now().year,
            month = Date.now().month,
            selectedDate = Date.now(),
            monthlyDataState = emptyList(),
            selectedMealIndex = 0,
            isLoading = false,
            selectedSchool = School.EmptySchool,
            isMealDialogVisible = false,
            isScheduleDialogVisible = false,
            mainUiMode = MainUiMode.LOADING,
        )
    }
}
