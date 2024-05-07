package com.practice.main.calendar

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.practice.designsystem.calendar.core.rememberCalendarState
import com.practice.main.MainScreenViewModel
import com.practice.main.mealColumns

@Composable
fun CalendarMainScreen(
    windowSize: WindowSizeClass,
    viewModel: MainScreenViewModel,
    onNavigateToSettingsScreen: () -> Unit,
    onNavigateToSelectSchoolScreen: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState

    val calendarState = rememberCalendarState(uiState.year, uiState.month, uiState.selectedDate)
    val calendarPageCount = 13

    val mealColumns = windowSize.mealColumns

    when (windowSize.widthSizeClass) {
        WindowWidthSizeClass.Expanded -> {
            HorizontalCalendarMainScreen(
                calendarPageCount = calendarPageCount,
                uiState = uiState,
                calendarState = calendarState,
                mealColumns = mealColumns,
                onRefreshIconClick = { viewModel.onRefreshIconClick(context) },
                onSettingsIconClick = onNavigateToSettingsScreen,
                onDateClick = viewModel::onDateClick,
                onSwiped = viewModel::onSwiped,
                getContentDescription = viewModel::getContentDescription,
                getClickLabel = viewModel::getClickLabel,
                drawUnderlineToScheduleDate = { },
                onNavigateToSelectSchoolScreen = onNavigateToSelectSchoolScreen,
                onMealTimeClick = viewModel::onMealTimeClick,
                onNutrientDialogOpen = viewModel::openNutrientDialog,
                onMemoDialogOpen = viewModel::openMemoDialog,
                modifier = modifier,
            ) { date -> viewModel.getCustomActions(date) }
        }

        else -> {
            VerticalCalendarMainScreen(
                calendarPageCount = calendarPageCount,
                uiState = uiState,
                calendarState = calendarState,
                mealColumns = mealColumns,
                onRefreshIconClick = { viewModel.onRefreshIconClick(context) },
                onSettingsIconClick = onNavigateToSettingsScreen,
                onDateClick = viewModel::onDateClick,
                onSwiped = viewModel::onSwiped,
                getContentDescription = viewModel::getContentDescription,
                getClickLabel = viewModel::getClickLabel,
                drawUnderlineToScheduleDate = {},
                onNavigateToSelectSchoolScreen = onNavigateToSelectSchoolScreen,
                onMealTimeClick = viewModel::onMealTimeClick,
                onNutrientDialogOpen = viewModel::openNutrientDialog,
                onMemoDialogOpen = viewModel::openMemoDialog,
                modifier = modifier,
            ) { date -> viewModel.getCustomActions(date) }
        }
    }
}