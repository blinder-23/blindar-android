package com.practice.main.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.practice.designsystem.a11y.isLargeFont
import com.practice.designsystem.components.BlindarDialog
import com.practice.designsystem.components.dialogContentPadding
import com.practice.main.main.calendar.CalendarMainScreen
import com.practice.main.main.daily.DailyMainScreen
import com.practice.main.main.dialog.MealDialogContents
import com.practice.main.main.dialog.ScheduleDialogContents
import com.practice.main.main.loading.LoadingMainScreen
import com.practice.main.main.state.MainUiMode
import com.practice.main.main.state.MainUiState
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreen(
    windowSize: WindowSizeClass,
    viewModel: MainScreenViewModel,
    onNavigateToSelectSchoolScreen: () -> Unit,
    onNavigateToSettingsScreen: () -> Unit,
    onNavigateToNutrientScreen: (MainUiState) -> Unit,
    onNavigateToMemoScreen: (MainUiState) -> Unit,
    modifier: Modifier = Modifier,
    onLaunch: suspend () -> Unit = {},
) {
    LaunchedEffect(Unit) {
        onLaunch()
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val backgroundModifier = modifier.background(MaterialTheme.colorScheme.surface)

    val mealPagerState = rememberPagerState { uiState.selectedDateDataState.uiMeals.mealTimes.size }

    Scaffold {
        val paddingModifier = backgroundModifier.padding(it)
        when (uiState.mainUiMode) {
            is MainUiMode.Loading -> {
                LoadingMainScreen(
                    modifier = paddingModifier,
                )
            }

            is MainUiMode.Calendar -> {
                CalendarMainScreen(
                    windowSize = windowSize,
                    viewModel = viewModel,
                    mealPagerState = mealPagerState,
                    onNavigateToSettingsScreen = onNavigateToSettingsScreen,
                    onNavigateToSelectSchoolScreen = onNavigateToSelectSchoolScreen,
                    onNavigateToNutrientScreen = onNavigateToNutrientScreen,
                    onNavigateToMemoScreen = onNavigateToMemoScreen,
                    modifier = paddingModifier,
                )
            }

            is MainUiMode.Daily -> {
                DailyMainScreen(
                    windowSize = windowSize,
                    viewModel = viewModel,
                    mealPagerState = mealPagerState,
                    onNavigateToSettingsScreen = onNavigateToSettingsScreen,
                    onNavigateToSelectSchoolScreen = onNavigateToSelectSchoolScreen,
                    onNavigateToNutrientScreen = onNavigateToNutrientScreen,
                    onNavigateToMemoScreen = onNavigateToMemoScreen,
                    modifier = paddingModifier,
                )
            }
        }
    }
    MainScreenDialogs(
        viewModel = viewModel,
        mealPagerState = mealPagerState,
        navigateToNutrientPage = onNavigateToNutrientScreen,
        navigateToMemoPage = onNavigateToMemoScreen,
    )
}

val WindowSizeClass.mealColumns: Int
    @Composable get() = when {
        LocalDensity.current.isLargeFont -> 1
        this.widthSizeClass == WindowWidthSizeClass.Compact -> 2
        else -> 3
    }

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun MainScreenDialogs(
    viewModel: MainScreenViewModel,
    mealPagerState: PagerState,
    navigateToNutrientPage: (MainUiState) -> Unit,
    navigateToMemoPage: (MainUiState) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    if (uiState.isMealDialogVisible) {
        val scope = rememberCoroutineScope()
        BlindarDialog(
            onDismissRequest = viewModel::onMealDialogClose,
            properties = DialogProperties(usePlatformDefaultWidth = false),
        ) {
            MealDialogContents(
                uiMeals = uiState.selectedDateDataState.uiMeals,
                mealPagerState = mealPagerState,
                onMealTimeClick = {
                    viewModel.onMealTimeClick(it)
                    scope.launch { mealPagerState.animateScrollToPage(it) }
                },
                onNutrientDialogOpen = { navigateToNutrientPage(uiState) },
                onMealDialogClose = viewModel::onMealDialogClose,
                modifier = Modifier
                    .padding(dialogContentPadding)
                    .widthIn(max = 600.dp),
            )
        }
    }

    if (uiState.isScheduleDialogVisible) {
        BlindarDialog(
            onDismissRequest = viewModel::onScheduleDialogClose,
            properties = DialogProperties(usePlatformDefaultWidth = false),
        ) {
            ScheduleDialogContents(
                scheduleElements = uiState.selectedDateDataState.memoDialogElements,
                onMemoDialogOpen = { navigateToMemoPage(uiState) },
                onScheduleDialogClose = viewModel::onScheduleDialogClose,
                modifier = Modifier
                    .padding(dialogContentPadding)
                    .widthIn(max = 600.dp),
            )
        }
    }
}