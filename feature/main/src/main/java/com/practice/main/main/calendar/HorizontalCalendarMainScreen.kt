package com.practice.main.main.calendar

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.CustomAccessibilityAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hsk.ktx.date.Date
import com.practice.designsystem.calendar.core.CalendarState
import com.practice.designsystem.calendar.core.YearMonth
import com.practice.designsystem.calendar.core.rememberCalendarState
import com.practice.designsystem.calendar.largeCalendarDateShape
import com.practice.designsystem.theme.BlindarTheme
import com.practice.main.R
import com.practice.main.main.MainScreenContents
import com.practice.main.main.MainScreenTopBar
import com.practice.main.main.previewMainUiState
import com.practice.main.main.state.MainUiState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HorizontalCalendarMainScreen(
    calendarPageCount: Int,
    uiState: MainUiState,
    calendarState: CalendarState,
    mealPagerState: PagerState,
    onRefreshIconClick: () -> Unit,
    onSettingsIconClick: () -> Unit,
    onCalendarHeaderClick: () -> Unit,
    calendarHeaderClickLabel: String,
    onDateClick: (Date) -> Unit,
    onSwiped: (YearMonth) -> Unit,
    getContentDescription: (Date) -> String,
    getClickLabel: (Date) -> String,
    drawUnderlineToScheduleDate: DrawScope.(Date) -> Unit,
    onNavigateToSelectSchoolScreen: () -> Unit,
    onMealTimeClick: (Int) -> Unit,
    onNutrientButtonClick: (MainUiState) -> Unit,
    onMemoButtonClick: (MainUiState) -> Unit,
    modifier: Modifier = Modifier,
    customActions: (Date) -> ImmutableList<CustomAccessibilityAction> = { persistentListOf() },
) {
    Column(modifier = modifier) {
        MainScreenTopBar(
            schoolName = uiState.selectedSchool.name,
            isLoading = uiState.isLoading,
            onRefreshIconClick = onRefreshIconClick,
            onSettingsIconClick = onSettingsIconClick,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
            onSchoolNameClick = onNavigateToSelectSchoolScreen,
            onClickLabel = stringResource(id = R.string.navigate_to_school_select)
        )
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            CalendarCard(
                calendarPageCount = calendarPageCount,
                calendarState = calendarState,
                calendarHeaderClickLabel = calendarHeaderClickLabel,
                onCalendarHeaderClick = onCalendarHeaderClick,
                onDateClick = onDateClick,
                onSwiped = onSwiped,
                getContentDescription = getContentDescription,
                dateShape = largeCalendarDateShape,
                getClickLabel = getClickLabel,
                drawBehindElement = drawUnderlineToScheduleDate,
                modifier = Modifier.weight(1f),
                customActions = customActions,
                isLarge = true,
            )
            MainScreenContents(
                uiMeals = uiState.selectedDateDataState.uiMeals,
                mealPagerState = mealPagerState,
                memoDialogElements = uiState.selectedDateDataState.memoDialogElements,
                onMealTimeClick = onMealTimeClick,
                onNutrientButtonClick = { onNutrientButtonClick(uiState) },
                onMemoButtonClick = { onMemoButtonClick(uiState) },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Preview(showBackground = true, device = "spec:width=1280dp,height=800dp,dpi=480")
@Composable
private fun HorizontalCalendarMainScreenPreview() {
    val selectedDate = Date.now()
    val (year, month, _) = selectedDate

    val uiState = previewMainUiState()
    val calendarState = rememberCalendarState(
        year = year,
        month = month,
        selectedDate = selectedDate,
    )
    val mealPagerState = rememberPagerState { 3 }
    BlindarTheme {
        HorizontalCalendarMainScreen(
            calendarPageCount = 13,
            uiState = uiState,
            mealPagerState = mealPagerState,
            calendarState = calendarState,
            onRefreshIconClick = {},
            onSettingsIconClick = {},
            onCalendarHeaderClick = {},
            calendarHeaderClickLabel = "",
            onDateClick = {},
            onSwiped = { },
            getContentDescription = { "" },
            getClickLabel = { "" },
            drawUnderlineToScheduleDate = {},
            onNavigateToSelectSchoolScreen = {},
            onMealTimeClick = {},
            onNutrientButtonClick = {},
            onMemoButtonClick = {},
            modifier = Modifier.background(MaterialTheme.colorScheme.surface),
        )
    }
}