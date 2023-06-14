package com.practice.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hsk.ktx.date.Date
import com.practice.designsystem.calendar.calendarDateShape
import com.practice.designsystem.calendar.core.CalendarState
import com.practice.designsystem.calendar.core.YearMonth
import com.practice.designsystem.calendar.core.rememberCalendarState
import com.practice.designsystem.theme.BlindarTheme
import com.practice.main.state.DailyMealScheduleState
import com.practice.main.state.MainUiState
import com.practice.main.state.MealUiState
import com.practice.main.state.ScheduleUiState
import com.practice.preferences.ScreenMode

@Composable
fun VerticalMainScreen(
    calendarPageCount: Int,
    uiState: MainUiState,
    calendarState: CalendarState,
    mealColumns: Int,
    onDateClick: (Date) -> Unit,
    onSwiped: (YearMonth) -> Unit,
    getContentDescription: (Date) -> String,
    getClickLabel: (Date) -> String,
    drawUnderlineToScheduleDate: DrawScope.(Date) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        MainScreenTopBar(
            schoolName = "한빛맹학교",
            modifier = Modifier
                .fillMaxWidth(2 / 3f)
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 24.dp),
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            CalendarCard(
                calendarPageCount = calendarPageCount,
                calendarState = calendarState,
                onDateClick = onDateClick,
                onSwiped = onSwiped,
                getContentDescription = getContentDescription,
                dateShape = calendarDateShape,
                getClickLabel = getClickLabel,
                drawBehindElement = drawUnderlineToScheduleDate,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
            )
            MainScreenContents(
                mealUiState = uiState.selectedDateMealScheduleState.mealUiState,
                scheduleUiState = uiState.selectedDateMealScheduleState.scheduleUiState,
                mealColumns = mealColumns,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
            )
        }
    }
}

@Preview(name = "Phone", device = "spec:width=411dp,height=891dp")
@Preview(name = "Foldable", device = "spec:width=673.5dp,height=841dp,dpi=480")
@Composable
private fun VerticalMainScreenPreview() {
    val year = 2022
    val month = 10
    var selectedDate by remember { mutableStateOf(Date(2022, 10, 11)) }

    val uiState by remember {
        mutableStateOf(
            MainUiState(
                year = year,
                month = month,
                selectedDate = selectedDate,
                monthlyMealScheduleState = (0..3).map {
                    DailyMealScheduleState(
                        date = Date(2022, 10, 11).plusDays(it),
                        mealUiState = MealUiState(previewMenus),
                        scheduleUiState = ScheduleUiState(previewSchedules),
                    )
                },
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
    BlindarTheme {
        VerticalMainScreen(
            calendarPageCount = 13,
            modifier = Modifier.background(MaterialTheme.colorScheme.surface),
            uiState = uiState,
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