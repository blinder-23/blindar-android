package com.practice.hanbitlunch.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.zIndex
import com.hsk.ktx.date.Date
import com.practice.hanbitlunch.calendar.SwipeableCalendar
import com.practice.hanbitlunch.calendar.calendarDateShape
import com.practice.hanbitlunch.calendar.core.CalendarState
import com.practice.hanbitlunch.calendar.core.YearMonth
import com.practice.hanbitlunch.calendar.core.rememberCalendarState
import com.practice.hanbitlunch.screen.core.DailyMealScheduleState
import com.practice.hanbitlunch.screen.core.MainUiState
import com.practice.hanbitlunch.screen.core.MealUiState
import com.practice.hanbitlunch.screen.core.ScheduleUiState
import com.practice.hanbitlunch.theme.HanbitCalendarTheme
import com.practice.preferences.ScreenMode

@Composable
fun VerticalMainScreen(
    modifier: Modifier = Modifier,
    uiState: MainUiState,
    dailyMealScheduleState: List<DailyMealScheduleState>,
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
            onRefresh = onRefresh,
            selectedScreenMode = uiState.screenMode,
            onScreenModeIconClick = onScreenModeChange,
            modifier = Modifier
                .weight(11f)
                .zIndex(1f),
        )
        Column(modifier = Modifier.weight(69f)) {
            AnimatedVisibility(
                visible = uiState.screenMode != ScreenMode.List,
                modifier = Modifier
                    .animateContentSize()
                    .weight(29f),
                enter = slideInVertically { fullHeight -> -fullHeight },
                exit = slideOutVertically { fullHeight -> -fullHeight },
            ) {
                SwipeableCalendar(
                    calendarState = calendarState,
                    onDateClick = onDateClick,
                    onSwiped = onSwiped,
                    getContentDescription = getContentDescription,
                    dateShape = calendarDateShape,
                    getClickLabel = getClickLabel,
                    drawBehindElement = drawUnderlineToScheduleDate,
                    modifier = Modifier
                )
            }
            DailyMealSchedules(
                items = dailyMealScheduleState,
                selectedDate = uiState.selectedDate,
                mealColumns = mealColumns,
                modifier = Modifier
                    .animateContentSize()
                    .weight(40f),
                onDateClick = onDateClick,
            )
        }
    }
}

@Preview(name = "Phone", device = "spec:width=411dp,height=891dp")
@Preview(name = "Foldable", device = "spec:width=673.5dp,height=841dp,dpi=480")
@Composable
private fun VerticalMainScreenPreview() {
    HanbitCalendarTheme {
        val year = 2022
        val month = 10
        var selectedDate by remember { mutableStateOf(Date(2022, 10, 11)) }

        var uiState by remember {
            mutableStateOf(
                MainUiState(
                    year = year,
                    month = month,
                    selectedDate = selectedDate,
                    mealUiState = MealUiState(previewMenus),
                    scheduleUiState = ScheduleUiState(previewSchedules),
                    isLoading = false,
                    screenMode = ScreenMode.Default,
                )
            )
        }
        val calendarState = rememberCalendarState(
            year = year,
            month = month,
            selectedDate = selectedDate,
        )
        HanbitCalendarTheme {
            VerticalMainScreen(
                modifier = Modifier.background(MaterialTheme.colors.surface),
                uiState = uiState,
                dailyMealScheduleState = (1..3).map {
                    DailyMealScheduleState.sample.copy(date = Date(2022, 10, 11).plusDays(it))
                },
                onRefresh = {},
                onScreenModeChange = { uiState = uiState.copy(screenMode = it) },
                calendarState = calendarState,
                mealColumns = 2,
                onDateClick = { selectedDate = it },
                onSwiped = {},
                getContentDescription = { "" },
                getClickLabel = { "" },
                drawUnderlineToScheduleDate = {},
            )
        }
    }
}