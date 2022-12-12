package com.practice.hanbitlunch.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.hsk.ktx.date.Date
import com.practice.hanbitlunch.calendar.SwipeableCalendar
import com.practice.hanbitlunch.calendar.calendarDateShape
import com.practice.hanbitlunch.calendar.core.CalendarState
import com.practice.hanbitlunch.calendar.core.YearMonth
import com.practice.hanbitlunch.calendar.core.drawUnderline
import com.practice.hanbitlunch.calendar.core.rememberCalendarState
import com.practice.hanbitlunch.calendar.largeCalendarDateShape
import com.practice.hanbitlunch.theme.HanbitCalendarTheme
import com.practice.preferences.ScreenMode

@Composable
fun MainScreen(
    windowSize: WindowSizeClass,
    viewModel: MainScreenViewModel,
    modifier: Modifier = Modifier,
    onLaunch: suspend () -> Unit = {},
    onRefresh: () -> Unit = {},
) {
    val systemUiController = rememberSystemUiController()
    val systemBarColor = MaterialTheme.colors.primary
    LaunchedEffect(true) {
        systemUiController.setStatusBarColor(systemBarColor)
        onLaunch()
        viewModel.onLaunch()
    }

    val uiState = viewModel.uiState.value
    val calendarState = rememberCalendarState()

    val scheduleDates by viewModel.scheduleDates.collectAsState()
    val underlineColor = MaterialTheme.colors.onSurface
    val drawUnderlineToScheduleDate: DrawScope.(Date) -> Unit = {
        if (scheduleDates.contains(it)) {
            drawUnderline(
                color = underlineColor,
                strokeWidth = 3f,
            )
        }
    }

    val backgroundModifier = modifier.background(MaterialTheme.colors.surface)
    val mealColumns = if (windowSize.widthSizeClass == WindowWidthSizeClass.Compact) 2 else 3
    if (windowSize.widthSizeClass == WindowWidthSizeClass.Expanded) {
        HorizontalMainScreen(
            modifier = backgroundModifier,
            uiState = uiState,
            onRefresh = onRefresh,
            onScreenModeChange = viewModel::onScreenModeChange,
            calendarState = calendarState,
            mealColumns = mealColumns,
            onDateClick = viewModel::onDateClick,
            onSwiped = viewModel::onSwiped,
            getContentDescription = viewModel::getContentDescription,
            getClickLabel = viewModel::getClickLabel,
            drawUnderlineToScheduleDate = drawUnderlineToScheduleDate,
        )
    } else {
        VerticalMainScreen(
            modifier = backgroundModifier,
            uiState = uiState,
            onRefresh = onRefresh,
            onScreenModeChange = viewModel::onScreenModeChange,
            calendarState = calendarState,
            mealColumns = mealColumns,
            onDateClick = viewModel::onDateClick,
            onSwiped = viewModel::onSwiped,
            getContentDescription = viewModel::getContentDescription,
            getClickLabel = viewModel::getClickLabel,
            drawUnderlineToScheduleDate = drawUnderlineToScheduleDate,
        )
    }
}

@Composable
private fun HorizontalMainScreen(
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
            MainScreenContents(
                mealUiState = uiState.mealUiState,
                scheduleUiState = uiState.scheduleUiState,
                modifier = Modifier.weight(1f),
                mealColumns = mealColumns,
            )
        }
    }
}

@Composable
private fun VerticalMainScreen(
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
            modifier = Modifier.weight(1f),
            mealColumns = mealColumns,
        )
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

@Preview(showBackground = true)
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