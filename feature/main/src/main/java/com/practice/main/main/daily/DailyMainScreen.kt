package com.practice.main.main.daily

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.practice.main.main.MainScreenViewModel
import com.practice.main.main.daily.picker.rememberDailyDatePickerState
import com.practice.main.main.daily.picker.toTextFieldFormat
import com.practice.main.main.mealColumns
import com.practice.main.main.state.MainUiState
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DailyMainScreen(
    windowSize: WindowSizeClass,
    viewModel: MainScreenViewModel,
    mealPagerState: PagerState,
    onNavigateToSettingsScreen: () -> Unit,
    onNavigateToSelectSchoolScreen: () -> Unit,
    onNavigateToNutrientScreen: (MainUiState) -> Unit,
    onNavigateToMemoScreen: (MainUiState) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val mealColumns = windowSize.mealColumns

    val datePickerState = rememberDailyDatePickerState(
        initialDate = uiState.selectedDate,
        initialTextFieldValue = uiState.selectedDate.toTextFieldFormat(),
    )

    val coroutineScope = rememberCoroutineScope()
    val onMealTimeClick: (Int) -> Unit = { index ->
        viewModel.onMealTimeClick(index)
        coroutineScope.launch { mealPagerState.animateScrollToPage(index) }
    }

    LaunchedEffect(datePickerState.executeDateCallback) {
        if (datePickerState.executeDateCallback) {
            viewModel.onDateClick(datePickerState.selectedDate)
            datePickerState.afterDateCallback()
        }
    }

    when (windowSize.widthSizeClass) {
        WindowWidthSizeClass.Expanded -> {
            HorizontalDailyMainScreen(
                uiState = uiState,
                mealPagerState = mealPagerState,
                datePickerState = datePickerState,
                mealColumns = mealColumns,
                onRefreshIconClick = { viewModel.onRefreshIconClick(context) },
                onSettingsIconClick = onNavigateToSettingsScreen,
                onSchoolNameClick = onNavigateToSelectSchoolScreen,
                onMealTimeClick = onMealTimeClick,
                onNutrientButtonClick = onNavigateToNutrientScreen,
                onMemoButtonClick = onNavigateToMemoScreen,
                onMealDialogOpen = viewModel::onMealDialog,
                onScheduleDialogOpen = viewModel::onScheduleDialogOpen,
                modifier = modifier,
            )
        }

        else -> {
            VerticalDailyMainScreen(
                uiState = uiState,
                mealPagerState = mealPagerState,
                datePickerState = datePickerState,
                onRefreshIconClick = { viewModel.onRefreshIconClick(context) },
                onSettingsIconClick = onNavigateToSettingsScreen,
                onSchoolNameClick = onNavigateToSelectSchoolScreen,
                onMealTimeClick = onMealTimeClick,
                onNutrientButtonClick = onNavigateToNutrientScreen,
                onMemoButtonClick = onNavigateToMemoScreen,
                onMealDialogOpen = viewModel::onMealDialog,
                onScheduleDialogOpen = viewModel::onScheduleDialogOpen,
                modifier = modifier,
            )
        }
    }
}