package com.practice.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.practice.designsystem.a11y.isLargeFont
import com.practice.designsystem.components.popupPadding
import com.practice.main.calendar.CalendarMainScreen
import com.practice.main.daily.DailyMainScreen
import com.practice.main.loading.LoadingMainScreen
import com.practice.main.popup.MealPopup
import com.practice.main.popup.MemoPopup
import com.practice.main.popup.NutrientPopup
import com.practice.main.popup.SchedulePopup
import com.practice.main.state.MainUiMode

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

    val uiState by viewModel.uiState

    val backgroundModifier = modifier.background(MaterialTheme.colorScheme.surface)

    Scaffold {
        val paddingModifier = backgroundModifier.padding(it)
        when (uiState.mainUiMode) {
            MainUiMode.LOADING -> {
                LoadingMainScreen(
                    modifier = paddingModifier,
                )
            }

            // TODO: 앱을 처음 켤 때 팝업 보여주지 말기
            // TODO: 설정 화면에서 달력 모드를 켤 때 팝업 보여주기 (정말 이 모드를 사용하시겠습니까? 스크린 리더에 불편할 수 있습니다...)

            MainUiMode.CALENDAR -> {
                CalendarMainScreen(
                    windowSize = windowSize,
                    viewModel = viewModel,
                    onNavigateToSettingsScreen = onNavigateToSettingsScreen,
                    onNavigateToSelectSchoolScreen = onNavigateToSelectSchoolScreen,
                    modifier = paddingModifier,
                )
            }

            MainUiMode.DAILY -> {
                DailyMainScreen(
                    windowSize = windowSize,
                    viewModel = viewModel,
                    onNavigateToSettingsScreen = onNavigateToSettingsScreen,
                    onNavigateToSelectSchoolScreen = onNavigateToSelectSchoolScreen,
                    modifier = paddingModifier,
                )
            }
        }
    }
    MainScreenPopups(
        viewModel = viewModel,
        mealColumns = windowSize.mealColumns,
    )
}

val WindowSizeClass.mealColumns: Int
    @Composable get() = when {
        LocalDensity.current.isLargeFont -> 1
        this.widthSizeClass == WindowWidthSizeClass.Compact -> 2
        else -> 3
    }

@Composable
private fun MainScreenPopups(
    viewModel: MainScreenViewModel,
    mealColumns: Int,
) {
    val uiState by viewModel.uiState

    if (uiState.isNutrientPopupVisible) {
        val selectedMealIndex = uiState.selectedMealIndex
        val selectedMeal = uiState.selectedDateDataState.uiMeals[selectedMealIndex]
        MainScreenPopup(
            onClose = viewModel::closeNutrientPopup,
        ) {
            NutrientPopup(
                uiMeal = selectedMeal,
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

    if (uiState.isMealPopupVisible) {
        MainScreenPopup(onClose = viewModel::onMealPopupClose) {
            MealPopup(
                uiMeals = uiState.selectedDateDataState.uiMeals,
                selectedMealIndex = uiState.selectedMealIndex,
                onMealTimeClick = viewModel::onMealTimeClick,
                mealColumns = mealColumns,
                onNutrientPopupOpen = viewModel::openNutrientPopup,
                onMealPopupClose = viewModel::onMealPopupClose,
                modifier = Modifier
                    .padding(popupPadding)
                    .widthIn(max = 600.dp),
            )
        }
    }

    if (uiState.isSchedulePopupVisible) {
        MainScreenPopup(onClose = viewModel::onSchedulePopupClose) {
            SchedulePopup(
                scheduleElements = uiState.selectedDateDataState.memoPopupElements,
                onMemoPopupOpen = viewModel::openMemoPopup,
                onSchedulePopupClose = viewModel::onSchedulePopupClose,
                modifier = Modifier
                    .padding(popupPadding)
                    .widthIn(max = 600.dp),
            )
        }
    }
}