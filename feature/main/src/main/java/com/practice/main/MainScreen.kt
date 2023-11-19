package com.practice.main

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.practice.designsystem.calendar.core.rememberCalendarState
import com.practice.designsystem.theme.BlindarTheme
import com.practice.main.popup.MemoPopup
import com.practice.main.popup.popupPadding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    windowSize: WindowSizeClass,
    viewModel: MainScreenViewModel,
    modifier: Modifier = Modifier,
    onLaunch: suspend () -> Unit = {},
    onNavigateToSelectSchoolScreen: () -> Unit = {},
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

    Scaffold {
        val paddingModifier = backgroundModifier.padding(it)
        if (windowSize.widthSizeClass == WindowWidthSizeClass.Expanded) {
            HorizontalMainScreen(
                calendarPageCount = calendarPageCount,
                uiState = uiState,
                onScreenModeChange = viewModel::onScreenModeChange,
                calendarState = calendarState,
                mealColumns = mealColumns,
                onDateClick = viewModel::onDateClick,
                onSwiped = viewModel::onSwiped,
                getContentDescription = viewModel::getContentDescription,
                getClickLabel = viewModel::getClickLabel,
                drawUnderlineToScheduleDate = { },
                onNutrientPopupOpen = viewModel::openNutrientPopup,
                onNutrientPopupClose = viewModel::closeNutrientPopup,
                onMemoPopupOpen = viewModel::openMemoPopup,
                modifier = paddingModifier,
            ) { date -> viewModel.getCustomActions(date) }
        } else {
            VerticalMainScreen(
                calendarPageCount = calendarPageCount,
                uiState = uiState,
                calendarState = calendarState,
                mealColumns = mealColumns,
                onDateClick = viewModel::onDateClick,
                onSwiped = viewModel::onSwiped,
                getContentDescription = viewModel::getContentDescription,
                getClickLabel = viewModel::getClickLabel,
                drawUnderlineToScheduleDate = {},
                onNavigateToSelectSchoolScreen = onNavigateToSelectSchoolScreen,
                onNutrientPopupOpen = viewModel::openNutrientPopup,
                onNutrientPopupClose = viewModel::closeNutrientPopup,
                onMemoPopupOpen = viewModel::openMemoPopup,
                modifier = paddingModifier,
            ) { date -> viewModel.getCustomActions(date) }
        }
    }
    if (uiState.isMemoPopupVisible) {
        MainScreenPopup(onClose = viewModel::closeMemoPopup) {
            MemoPopup(
                date = uiState.selectedDateDataState.memoUiState.date,
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

@Composable
fun FloatingRefreshButton(
    isLoading: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val refreshIconAlpha by animateFloatAsState(targetValue = if (isLoading) 0.5f else 1f)
    val infiniteTransition = rememberInfiniteTransition()
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = if (isLoading) 180f else 0f,
        animationSpec = InfiniteRepeatableSpec(
            animation = tween(
                durationMillis = 750,
                easing = CubicBezierEasing(0.3f, 0f, 0.7f, 1f),
            ),
        )
    )

    val interactionSource = remember { MutableInteractionSource() }
    FloatingActionButton(
        interactionSource = interactionSource,
        onClick = {
            if (!isLoading) onRefresh()
        },
        modifier = modifier
            .rotate(angle),
        containerColor = MaterialTheme.colorScheme.primary,
    ) {
        RefreshIcon(
            iconAlpha = { refreshIconAlpha },
        )
    }
}

@Preview
@Composable
private fun FloatingRefreshButtonPreview() {
    var isLoading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    BlindarTheme {
        FloatingRefreshButton(
            isLoading = isLoading,
            onRefresh = {
                isLoading = !isLoading
                coroutineScope.launch {
                    delay(2500L)
                    isLoading = !isLoading
                }
            },
        )
    }
}