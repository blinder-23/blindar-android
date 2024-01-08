package com.practice.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.practice.designsystem.calendar.core.rememberCalendarState
import com.practice.main.calendar.HorizontalCalendarMainScreen
import com.practice.main.calendar.VerticalCalendarMainScreen
import com.practice.main.popup.MemoPopup
import com.practice.main.popup.NutrientPopup
import com.practice.main.popup.popupPadding

@Composable
fun MainScreen(
    windowSize: WindowSizeClass,
    viewModel: MainScreenViewModel,
    onNavigateToSelectSchoolScreen: () -> Unit,
    onNavigateToSettingsScreen: () -> Unit,
    modifier: Modifier = Modifier,
    onLaunch: suspend () -> Unit = {},
) {
    val systemUiController = rememberSystemUiController()
    val systemBarColor = MaterialTheme.colorScheme.surface
    LaunchedEffect(true) {
        systemUiController.setStatusBarColor(systemBarColor)
        systemUiController.setNavigationBarColor(systemBarColor)
        onLaunch()
        viewModel.onLaunch()
    }

    val calendarPageCount = 13

    val uiState by viewModel.uiState
    val calendarState = rememberCalendarState(uiState.year, uiState.month, uiState.selectedDate)

    val backgroundModifier = modifier.background(MaterialTheme.colorScheme.surface)
    val mealColumns = if (windowSize.widthSizeClass == WindowWidthSizeClass.Compact) 2 else 3

    val context = LocalContext.current
    Scaffold {
        val paddingModifier = backgroundModifier.padding(it)
        if (windowSize.widthSizeClass == WindowWidthSizeClass.Expanded) {
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
                onNutrientPopupOpen = viewModel::openNutrientPopup,
                onMemoPopupOpen = viewModel::openMemoPopup,
                modifier = paddingModifier,
            ) { date -> viewModel.getCustomActions(date) }
        } else {
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
                onNutrientPopupOpen = viewModel::openNutrientPopup,
                onMemoPopupOpen = viewModel::openMemoPopup,
                modifier = paddingModifier,
            ) { date -> viewModel.getCustomActions(date) }
        }
    }
    if (uiState.isNutrientPopupVisible) {
        val uiMeal = uiState.selectedDateDataState.uiMeal
        MainScreenPopup(
            onClose = viewModel::closeNutrientPopup,
        ) {
            NutrientPopup(
                uiMeal = uiMeal,
                nutrients = uiMeal.nutrients,
                onClose = viewModel::closeNutrientPopup,
                modifier = Modifier.padding(popupPadding),
            )
        }
    }
    if (uiState.isMemoPopupVisible) {
        MainScreenPopup(onClose = viewModel::closeMemoPopup) {
            MemoPopup(
                date = uiState.selectedDateDataState.uiMemos.date,
                memoPopupElements = uiState.selectedDateDataState.memoPopupElements,
                onAddMemo = viewModel::addMemo,
                onContentsChange = viewModel::updateMemoOnLocal,
                onMemoDelete = viewModel::deleteMemo,
                onPopupClose = viewModel::closeMemoPopup,
                modifier = Modifier.padding(popupPadding),
            )
        }
    }
}