package com.practice.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.practice.main.state.DailyData
import com.practice.main.state.MainUiState
import com.practice.main.state.MealUiState
import com.practice.main.state.MemoUiState
import com.practice.main.state.ScheduleUiState
import com.practice.preferences.ScreenMode
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun VerticalMainScreen(
    calendarPageCount: Int,
    uiState: MainUiState,
    calendarState: CalendarState,
    mealColumns: Int,
    onDateClick: (Date) -> Unit,
    onSwiped: (YearMonth) -> Unit,
    getContentDescription: (Date) -> String,
    getClickLabel: (Date) -> String,
    drawUnderlineToScheduleDate: DrawScope.(Date) -> Unit,
    onNavigateToSelectSchoolScreen: () -> Unit,
    isNutrientPopupVisible: Boolean,
    onNutrientPopupOpen: () -> Unit,
    onNutrientPopupClose: () -> Unit,
    modifier: Modifier = Modifier,
    customActions: (Date) -> ImmutableList<CustomAccessibilityAction> = { persistentListOf() },
) {
    Column(modifier = modifier) {
        MainScreenTopBar(
            schoolName = uiState.selectedSchool.name,
            modifier = Modifier
                .fillMaxWidth(2 / 3f)
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 12.dp),
            onClick = onNavigateToSelectSchoolScreen,
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
                onDateClick = onDateClick,
                onSwiped = onSwiped,
                getContentDescription = getContentDescription,
                dateShape = calendarDateShape,
                getClickLabel = getClickLabel,
                drawBehindElement = drawUnderlineToScheduleDate,
                customActions = customActions,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
            )
            MainScreenContents(
                mealUiState = uiState.selectedDateDataState.mealUiState,
                scheduleUiState = uiState.selectedDateDataState.scheduleUiState,
                mealColumns = mealColumns,
                isNutrientPopupVisible = isNutrientPopupVisible,
                onNutrientPopupOpen = onNutrientPopupOpen,
                onNutrientPopupClose = onNutrientPopupClose,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
            )
        }
    }
}

@Preview(name = "Phone", device = "spec:width=411dp,height=891dp")
@Preview(name = "Foldable", device = "spec:width=673.5dp,height=841dp,dpi=480")
@Composable
private fun VerticalMainScreenPreview() {
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
                        mealUiState = MealUiState(
                            year,
                            month,
                            now.dayOfMonth,
                            previewMenus,
                            previewNutrients
                        ),
                        scheduleUiState = ScheduleUiState(previewSchedules),
                        memoUiState = MemoUiState(2022, 10, 11, previewMemos),
                    )
                },
                isLoading = false,
                screenMode = ScreenMode.Default,
                selectedSchool = School(
                    name = "어떤 학교",
                    schoolCode = -1,
                ),
                isNutrientPopupVisible = false,
                isMemoPopupVisible = false,
            )
        )
    }
    val calendarState = rememberCalendarState(
        year = year,
        month = month,
        selectedDate = selectedDate,
    )
    BlindarTheme {
        VerticalMainScreen(
            calendarPageCount = 13,
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .fillMaxSize(),
            uiState = uiState,
            calendarState = calendarState,
            mealColumns = 2,
            onDateClick = { selectedDate = it },
            onSwiped = {},
            getContentDescription = { "" },
            getClickLabel = { "" },
            drawUnderlineToScheduleDate = {},
            onNavigateToSelectSchoolScreen = {},
            isNutrientPopupVisible = false,
            onNutrientPopupOpen = {},
            onNutrientPopupClose = {},
        )
    }
}