package com.practice.hanbitlunch.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.tooling.preview.Preview
import com.hsk.ktx.date.Date
import com.practice.hanbitlunch.calendar.SwipeableCalendar
import com.practice.hanbitlunch.calendar.core.CalendarState
import com.practice.hanbitlunch.calendar.core.YearMonth
import com.practice.hanbitlunch.calendar.core.rememberCalendarState
import com.practice.hanbitlunch.calendar.largeCalendarDateShape
import com.practice.hanbitlunch.screen.core.DailyMealScheduleState
import com.practice.hanbitlunch.screen.core.MainUiState
import com.practice.hanbitlunch.screen.core.MealUiState
import com.practice.hanbitlunch.screen.core.ScheduleUiState
import com.practice.hanbitlunch.theme.HanbitCalendarTheme
import com.practice.preferences.ScreenMode

@Composable
fun HorizontalMainScreen(
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
        MainScreenHeader(
            year = uiState.year,
            month = uiState.month,
            isLoading = uiState.isLoading,
            screenModeIconsEnabled = false,
            onRefresh = onRefresh,
            selectedScreenMode = uiState.screenMode,
            onScreenModeIconClick = onScreenModeChange,
        )
        Row {
            SwipeableCalendar(
                modifier = Modifier.weight(1f),
                calendarState = calendarState,
                onDateClick = onDateClick,
                onSwiped = onSwiped,
                getContentDescription = getContentDescription,
                getClickLabel = getClickLabel,
                drawBehindElement = drawUnderlineToScheduleDate,
                dateShape = largeCalendarDateShape,
                dateArrangement = Arrangement.Top,
            )
            DailyMealSchedules(
                items = uiState.monthlyMealScheduleState,
                selectedDate = uiState.selectedDate,
                onDateClick = onDateClick,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize(),
                mealColumns = mealColumns,
            )
        }
    }
}

@Preview(showBackground = true, device = "spec:width=1280dp,height=800dp,dpi=480")
@Composable
private fun HorizontalMainScreenPreview() {
    val year = 2022
    val month = 10
    val selectedDate = Date(2022, 10, 11)

    val uiState = MainUiState(
        year = year,
        month = month,
        selectedDate = selectedDate,
        monthlyMealScheduleState = (1..3).map {
            DailyMealScheduleState(
                date = Date(2022, 10, 11).plusDays(it),
                mealUiState = MealUiState(previewMenus),
                scheduleUiState = ScheduleUiState(previewSchedules),
            )
        },
        isLoading = false,
        screenMode = ScreenMode.Default,
    )
    val calendarState = rememberCalendarState(
        year = year,
        month = month,
        selectedDate = selectedDate,
    )
    HanbitCalendarTheme {
        HorizontalMainScreen(
            modifier = Modifier.background(MaterialTheme.colors.surface),
            uiState = uiState,
            onRefresh = {},
            onScreenModeChange = {},
            calendarState = calendarState,
            mealColumns = 3,
            onDateClick = {},
            onSwiped = { },
            getContentDescription = { "" },
            getClickLabel = { "" },
            drawUnderlineToScheduleDate = {},
        )
    }
}