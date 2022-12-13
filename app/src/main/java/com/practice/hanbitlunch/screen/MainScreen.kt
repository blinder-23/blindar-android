package com.practice.hanbitlunch.screen

import androidx.compose.foundation.background
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.hsk.ktx.date.Date
import com.practice.hanbitlunch.calendar.core.drawUnderline
import com.practice.hanbitlunch.calendar.core.rememberCalendarState

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