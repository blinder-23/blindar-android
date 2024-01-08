package com.practice.main.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.practice.domain.School
import com.practice.main.MainScreenContents
import com.practice.main.MainScreenTopBar
import com.practice.main.R
import com.practice.main.previewMemos
import com.practice.main.previewMenus
import com.practice.main.previewNutrients
import com.practice.main.previewSchedules
import com.practice.main.state.DailyData
import com.practice.main.state.MainUiMode
import com.practice.main.state.MainUiState
import com.practice.main.state.UiMeal
import com.practice.main.state.UiMemos
import com.practice.main.state.UiSchedules
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun HorizontalCalendarMainScreen(
    calendarPageCount: Int,
    uiState: MainUiState,
    calendarState: CalendarState,
    mealColumns: Int,
    onRefreshIconClick: () -> Unit,
    onSettingsIconClick: () -> Unit,
    onDateClick: (Date) -> Unit,
    onSwiped: (YearMonth) -> Unit,
    getContentDescription: (Date) -> String,
    getClickLabel: (Date) -> String,
    drawUnderlineToScheduleDate: DrawScope.(Date) -> Unit,
    onNavigateToSelectSchoolScreen: () -> Unit,
    onNutrientPopupOpen: () -> Unit,
    onNutrientPopupClose: () -> Unit,
    onMemoPopupOpen: () -> Unit,
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
                modifier = Modifier.weight(1f),
                calendarState = calendarState,
                onDateClick = onDateClick,
                onSwiped = onSwiped,
                getContentDescription = getContentDescription,
                getClickLabel = getClickLabel,
                drawBehindElement = drawUnderlineToScheduleDate,
                dateShape = largeCalendarDateShape,
                dateArrangement = Arrangement.Top,
                customActions = customActions,
            )
            MainScreenContents(
                uiMeal = uiState.selectedDateDataState.uiMeal,
                memoPopupElements = uiState.selectedDateDataState.memoPopupElements,
                mealColumns = mealColumns,
                isNutrientPopupVisible = uiState.isNutrientPopupVisible,
                onNutrientPopupOpen = onNutrientPopupOpen,
                onNutrientPopupClose = onNutrientPopupClose,
                onMemoPopupOpen = onMemoPopupOpen,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
            )
        }
    }
}

@Preview(showBackground = true, device = "spec:width=1280dp,height=800dp,dpi=480")
@Composable
private fun HorizontalCalendarMainScreenPreview() {
    val year = 2022
    val month = 10
    val selectedDate = Date(2022, 10, 11)

    val uiState = MainUiState(
        userId = "",
        year = year,
        month = month,
        selectedDate = selectedDate,
        monthlyDataState = (1..3).map {
            DailyData(
                schoolCode = 1,
                date = Date(2022, 10, 11).plusDays(it),
                uiMeal = UiMeal(2022, 10, 11, previewMenus, previewNutrients),
                uiSchedules = UiSchedules(
                    date = selectedDate,
                    uiSchedules = previewSchedules,
                ),
                uiMemos = UiMemos(
                    date = selectedDate,
                    memos = previewMemos,
                ),
            )
        },
        isLoading = false,
        selectedSchool = School(
            name = "어떤 학교",
            schoolCode = -1,
        ),
        isNutrientPopupVisible = false,
        isMemoPopupVisible = false,
        mainUiMode = MainUiMode.CALENDAR,
    )
    val calendarState = rememberCalendarState(
        year = year,
        month = month,
        selectedDate = selectedDate,
    )
    BlindarTheme {
        HorizontalCalendarMainScreen(
            calendarPageCount = 13,
            uiState = uiState,
            calendarState = calendarState,
            mealColumns = 3,
            onRefreshIconClick = {},
            onSettingsIconClick = {},
            onDateClick = {},
            onSwiped = { },
            getContentDescription = { "" },
            getClickLabel = { "" },
            drawUnderlineToScheduleDate = {},
            onNavigateToSelectSchoolScreen = {},
            onNutrientPopupOpen = {},
            onNutrientPopupClose = {},
            onMemoPopupOpen = {},
            modifier = Modifier.background(MaterialTheme.colorScheme.surface),
        )
    }
}