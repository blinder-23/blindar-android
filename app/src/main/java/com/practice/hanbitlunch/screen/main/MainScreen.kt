package com.practice.hanbitlunch.screen.main

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.hsk.ktx.date.Date
import com.practice.hanbitlunch.calendar.core.drawUnderline
import com.practice.hanbitlunch.calendar.core.rememberCalendarState
import com.practice.hanbitlunch.theme.BlindarTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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

    val uiState by viewModel.uiState
    val calendarState = rememberCalendarState(uiState.year, uiState.month, uiState.selectedDate)

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

    Scaffold(floatingActionButton = {
        FloatingRefreshButton(
            isLoading = uiState.isLoading,
            onRefresh = onRefresh,
        )
    }) {
        val paddingModifier = backgroundModifier.padding(it)
        if (windowSize.widthSizeClass == WindowWidthSizeClass.Expanded) {
            HorizontalMainScreen(
                modifier = paddingModifier,
                uiState = uiState,
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
                modifier = paddingModifier,
                uiState = uiState,
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

    FloatingActionButton(
        interactionSource = MutableInteractionSource(),
        onClick = onRefresh,
        modifier = modifier
            .padding(0.dp)
            .rotate(angle),
        backgroundColor = MaterialTheme.colors.primary,
    ) {
        RefreshIcon(
            isLoading = isLoading,
            onRefresh = onRefresh,
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