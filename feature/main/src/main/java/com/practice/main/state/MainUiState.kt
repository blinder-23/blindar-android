package com.practice.main.state

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
    val isNutrientPopupVisible: Boolean,
    val isMemoPopupVisible: Boolean,
    val isMealPopupVisible: Boolean,
    val isSchedulePopupVisible: Boolean,
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
        get() = selectedDateDataState.memoPopupElements.isNotEmpty()

    fun updateMemoUiState(date: Date, uiMemos: UiMemos): List<DailyData> {
        return if (monthlyDataState.any { it.date == date }) {
            addMemoUiStateOnExistingDate(date, uiMemos)
        } else {
            addMemoUiStateOnNewDate(date, uiMemos)
        }
    }

    private fun addMemoUiStateOnNewDate(
        date: Date,
        uiMemos: UiMemos
    ): List<DailyData> = monthlyDataState.toMutableList().apply {
        add(
            DailyData(
                schoolCode = selectedSchoolCode,
                date = date,
                uiMeals = UiMeals(),
                uiSchedules = UiSchedules.EmptyUiSchedules,
                uiMemos = uiMemos
            )
        )
    }.sortedBy { it.date }

    private fun addMemoUiStateOnExistingDate(
        date: Date,
        uiMemos: UiMemos
    ): List<DailyData> = monthlyDataState.map {
        if (it.date == date) {
            it.copy(uiMemos = uiMemos)
        } else {
            it
        }
    }

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
            isNutrientPopupVisible = false,
            isMemoPopupVisible = false,
            isMealPopupVisible = false,
            isSchedulePopupVisible = false,
            mainUiMode = MainUiMode.LOADING,
        )
    }
}
