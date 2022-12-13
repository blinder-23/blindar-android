package com.practice.hanbitlunch.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.tooling.preview.Preview
import com.hsk.ktx.date.Date
import com.practice.hanbitlunch.calendar.SwipeableCalendar
import com.practice.hanbitlunch.calendar.calendarDateShape
import com.practice.hanbitlunch.calendar.core.CalendarState
import com.practice.hanbitlunch.calendar.core.YearMonth
import com.practice.hanbitlunch.calendar.core.rememberCalendarState
import com.practice.hanbitlunch.theme.HanbitCalendarTheme
import com.practice.preferences.ScreenMode

@Composable
fun VerticalMainScreen(
    modifier: Modifier = Modifier,
    uiState: MainUiState,
    onRefresh: () -> Unit,
    onScreenModeChange: (ScreenMode) -> Unit,
    calendarState: CalendarState,
    mealColumns: Int,
    onDateClick: (Date) -> Unit,
    onSwiped: (YearMonth) -> Unit,
    getContentDescription: (Date) -> String,
    getClickLabel: (Date) -> String,
    drawUnderlineToScheduleDate: DrawScope.(Date) -> Unit,
) {
    Column(modifier = modifier) {
        Column(modifier = Modifier.weight(1f)) {
            MainScreenHeader(
                year = uiState.year,
                month = uiState.month,
                isLoading = uiState.isLoading,
                onRefresh = onRefresh,
                selectedScreenMode = uiState.screenMode,
                onScreenModeIconClick = onScreenModeChange,
            )
            SwipeableCalendar(
                calendarState = calendarState,
                onDateClick = onDateClick,
                onSwiped = onSwiped,
                getContentDescription = getContentDescription,
                dateShape = calendarDateShape,
                getClickLabel = getClickLabel,
                drawBehindElement = drawUnderlineToScheduleDate,
            )
        }
        MainScreenContents(
            mealUiState = uiState.mealUiState,
            scheduleUiState = uiState.scheduleUiState,
            modifier = Modifier
                .weight(1f)
                .fillMaxSize(),
            mealColumns = mealColumns,
        )
    }
}

@Preview(name = "Phone", device = "spec:width=411dp,height=891dp")
@Preview(name = "Foldable", device = "spec:width=673.5dp,height=841dp,dpi=480")
@Composable
private fun VerticalMainScreenPreview() {
    HanbitCalendarTheme {
        val year = 2022
        val month = 10
        val selectedDate = Date(2022, 10, 11)

        val uiState = MainUiState(
            year = year,
            month = month,
            selectedDate = selectedDate,
            mealUiState = MealUiState(previewMenus),
            scheduleUiState = ScheduleUiState(previewSchedules),
            isLoading = false,
            screenMode = ScreenMode.Default,
        )
        val calendarState = rememberCalendarState(
            year = year,
            month = month,
            selectedDate = selectedDate,
        )
        HanbitCalendarTheme {
            VerticalMainScreen(
                modifier = Modifier.background(MaterialTheme.colors.surface),
                uiState = uiState,
                onRefresh = {},
                onScreenModeChange = {},
                calendarState = calendarState,
                mealColumns = 2,
                onDateClick = {},
                onSwiped = {},
                getContentDescription = { "" },
                getClickLabel = { "" },
                drawUnderlineToScheduleDate = {},
            )
        }
    }
}