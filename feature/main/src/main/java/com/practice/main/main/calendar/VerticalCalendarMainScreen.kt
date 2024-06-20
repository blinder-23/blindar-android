package com.practice.main.main.calendar

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.CustomAccessibilityAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hsk.ktx.date.Date
import com.practice.designsystem.calendar.calendarDateShape
import com.practice.designsystem.calendar.core.CalendarState
import com.practice.designsystem.calendar.core.YearMonth
import com.practice.designsystem.calendar.core.rememberCalendarState
import com.practice.designsystem.calendar.core.yearMonth
import com.practice.designsystem.theme.BlindarTheme
import com.practice.domain.School
import com.practice.main.R
import com.practice.main.main.MainScreenContents
import com.practice.main.main.MainScreenTopBar
import com.practice.main.main.previewMemos
import com.practice.main.main.previewSchedules
import com.practice.main.main.previewUiMenus
import com.practice.main.main.previewUiNutrients
import com.practice.main.main.state.DailyData
import com.practice.main.main.state.MainUiMode
import com.practice.main.main.state.MainUiState
import com.practice.main.state.UiMeal
import com.practice.main.state.UiMeals
import com.practice.main.state.UiMemos
import com.practice.main.state.UiSchedules
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun VerticalCalendarMainScreen(
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
    onNutrientDialogOpen: () -> Unit,
    onMemoDialogOpen: () -> Unit,
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
            onClickLabel = stringResource(id = R.string.navigate_to_school_select),
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            CalendarCard(
                calendarPageCount = calendarPageCount,
                calendarState = calendarState,
                calendarHeaderClickLabel = calendarHeaderClickLabel,
                onCalendarHeaderClick = onCalendarHeaderClick,
                onDateClick = onDateClick,
                onSwiped = onSwiped,
                getContentDescription = getContentDescription,
                dateShape = calendarDateShape,
                getClickLabel = getClickLabel,
                drawBehindElement = drawUnderlineToScheduleDate,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                customActions = customActions,
            )
            MainScreenContents(
                uiMeals = uiState.selectedDateDataState.uiMeals,
                mealPagerState = mealPagerState,
                memoDialogElements = uiState.selectedDateDataState.memoDialogElements,
                onMealTimeClick = onMealTimeClick,
                onNutrientDialogOpen = onNutrientDialogOpen,
                onMemoDialogOpen = onMemoDialogOpen,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Preview(name = "Phone", device = "spec:width=411dp,height=891dp")
@Preview(name = "Foldable", device = "spec:width=673.5dp,height=841dp,dpi=480")
@Composable
private fun VerticalCalendarMainScreenPreview() {
    val now = Date.now()
    val (year, month) = now.yearMonth
    var selectedDate by remember { mutableStateOf(now) }

    val uiState by remember {
        mutableStateOf(
            MainUiState(
                userId = "",
                year = year,
                month = month,
                selectedDate = selectedDate,
                monthlyDataState = (0..3).map {
                    DailyData(
                        schoolCode = 1,
                        date = now.plusDays(it),
                        uiMeals = UiMeals(
                            UiMeal(
                                year,
                                month,
                                now.dayOfMonth,
                                "중식",
                                previewUiMenus,
                                previewUiNutrients,
                            )
                        ),
                        uiSchedules = UiSchedules(
                            date = now,
                            uiSchedules = previewSchedules,
                        ),
                        uiMemos = UiMemos(
                            date = now,
                            memos = previewMemos,
                        ),
                    )
                },
                selectedMealIndex = 0,
                isLoading = false,
                selectedSchool = School(
                    name = "어떤 학교",
                    schoolCode = -1,
                ),
                isNutrientDialogVisible = false,
                isMemoDialogVisible = false,
                isMealDialogVisible = false,
                isScheduleDialogVisible = false,
                mainUiMode = MainUiMode.CALENDAR,
            )
        )
    }
    val calendarState = rememberCalendarState(
        year = year,
        month = month,
        selectedDate = selectedDate,
    )
    val mealPagerState = rememberPagerState { 1 }
    BlindarTheme {
        VerticalCalendarMainScreen(
            calendarPageCount = 13,
            uiState = uiState,
            mealPagerState = mealPagerState,
            calendarState = calendarState,
            onRefreshIconClick = {},
            onSettingsIconClick = {},
            onCalendarHeaderClick = {},
            calendarHeaderClickLabel = "",
            onDateClick = { selectedDate = it },
            onSwiped = {},
            getContentDescription = { "" },
            getClickLabel = { "" },
            drawUnderlineToScheduleDate = {},
            onNavigateToSelectSchoolScreen = {},
            onMealTimeClick = {},
            onNutrientDialogOpen = {},
            onMemoDialogOpen = {},
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .fillMaxSize(),
        )
    }
}