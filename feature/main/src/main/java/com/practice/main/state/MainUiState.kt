package com.practice.main.state

import com.hsk.ktx.date.Date
import com.practice.designsystem.calendar.core.YearMonth
import com.practice.domain.School
import com.practice.preferences.ScreenMode

data class MainUiState(
    val userId: String,
    val year: Int,
    val month: Int,
    val selectedDate: Date,
    val monthlyDataState: List<DailyData>,
    val isLoading: Boolean,
    val screenMode: ScreenMode,
    val selectedSchool: School,
    val isNutrientPopupVisible: Boolean,
    val isMemoPopupVisible: Boolean,
) {
    val yearMonth: YearMonth
        get() = YearMonth(year, month)

    val selectedSchoolCode: Int
        get() = selectedSchool.schoolCode

    val selectedDateDataState: DailyData
        get() = monthlyDataState.firstOrNull { it.date == selectedDate }
            ?: DailyData.Empty

    companion object {
        val EMPTY = MainUiState(
            userId = "",
            year = Date.now().year,
            month = Date.now().month,
            selectedDate = Date.now(),
            monthlyDataState = emptyList(),
            isLoading = false,
            screenMode = ScreenMode.Default,
            selectedSchool = School.EmptySchool,
            isNutrientPopupVisible = false,
            isMemoPopupVisible = false,
        )
    }
}
