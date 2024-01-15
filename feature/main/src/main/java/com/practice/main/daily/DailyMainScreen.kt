package com.practice.main.daily

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.practice.main.MainScreenViewModel
import com.practice.main.daily.picker.rememberDailyDatePickerState
import com.practice.main.daily.picker.toTextFieldFormat
import com.practice.main.mealColumns

@Composable
fun DailyMainScreen(
    windowSize: WindowSizeClass,
    viewModel: MainScreenViewModel,
    onNavigateToSettingsScreen: () -> Unit,
    onNavigateToSelectSchoolScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState

    val mealColumns = windowSize.mealColumns

    val datePickerState = rememberDailyDatePickerState(
        initialDate = uiState.selectedDate,
        initialTextFieldValue = uiState.selectedDate.toTextFieldFormat(),
        onDateInput = viewModel::onDateClick,
    )

    when (windowSize.widthSizeClass) {
        WindowWidthSizeClass.Expanded -> {
            HorizontalDailyMainScreen(
                uiState = uiState,
                datePickerState = datePickerState,
                mealColumns = mealColumns,
                onRefreshIconClick = { viewModel.onRefreshIconClick(context) },
                onSettingsIconClick = onNavigateToSettingsScreen,
                onSchoolNameClick = onNavigateToSelectSchoolScreen,
                onMealTimeClick = viewModel::onMealTimeClick,
                onNutrientPopupOpen = viewModel::openNutrientPopup,
                onMemoPopupOpen = viewModel::openMemoPopup,
                onMealPopupOpen = viewModel::onMealPopupOpen,
                onSchedulePopupOpen = viewModel::onSchedulePopupOpen,
                modifier = modifier,
            )
        }

        else -> {
            VerticalDailyMainScreen(
                uiState = uiState,
                datePickerState = datePickerState,
                mealColumns = mealColumns,
                onRefreshIconClick = { viewModel.onRefreshIconClick(context) },
                onSettingsIconClick = onNavigateToSettingsScreen,
                onSchoolNameClick = onNavigateToSelectSchoolScreen,
                onMealTimeClick = viewModel::onMealTimeClick,
                onNutrientPopupOpen = viewModel::openNutrientPopup,
                onMemoPopupOpen = viewModel::openMemoPopup,
                onMealPopupOpen = viewModel::onMealPopupOpen,
                onSchedulePopupOpen = viewModel::onSchedulePopupOpen,
                modifier = modifier,
            )
        }
    }
}