package com.practice.main.main.daily

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.hsk.ktx.date.Date
import com.practice.designsystem.LightPreview
import com.practice.designsystem.theme.BlindarTheme
import com.practice.domain.School
import com.practice.main.R
import com.practice.main.main.MainScreenContents
import com.practice.main.main.MainScreenTopBar
import com.practice.main.main.daily.components.DateQuickNavigation
import com.practice.main.main.daily.components.DateQuickNavigationButtons
import com.practice.main.main.daily.components.QuickViewDialogButtons
import com.practice.main.main.daily.picker.DailyDatePicker
import com.practice.main.main.daily.picker.DailyDatePickerState
import com.practice.main.main.daily.picker.rememberDailyDatePickerState
import com.practice.main.main.daily.picker.toTextFieldFormat
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
import kotlin.math.absoluteValue

@Composable
fun VerticalDailyMainScreen(
    uiState: MainUiState,
    mealPagerState: PagerState,
    datePickerState: DailyDatePickerState,
    onRefreshIconClick: () -> Unit,
    onSettingsIconClick: () -> Unit,
    onSchoolNameClick: () -> Unit,
    onMealTimeClick: (Int) -> Unit,
    onNutrientButtonClick: (MainUiState) -> Unit,
    onMemoButtonClick: (MainUiState) -> Unit,
    onMealDialogOpen: () -> Unit,
    onScheduleDialogOpen: () -> Unit,
    modifier: Modifier = Modifier,
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
            onSchoolNameClick = onSchoolNameClick,
            onClickLabel = stringResource(id = R.string.navigate_to_school_select),
        )
        VerticalDailyMainScreenContents(
            datePickerState = datePickerState,
            mealPagerState = mealPagerState,
            uiState = uiState,
            onMealTimeClick = onMealTimeClick,
            onNutrientButtonClick = onNutrientButtonClick,
            onMemoButtonClick = onMemoButtonClick,
            onMealDialogOpen = onMealDialogOpen,
            onScheduleDialogOpen = onScheduleDialogOpen,
        )
    }
}

@Composable
private fun VerticalDailyMainScreenContents(
    datePickerState: DailyDatePickerState,
    uiState: MainUiState,
    mealPagerState: PagerState,
    onMealTimeClick: (Int) -> Unit,
    onNutrientButtonClick: (MainUiState) -> Unit,
    onMemoButtonClick: (MainUiState) -> Unit,
    onMealDialogOpen: () -> Unit,
    onScheduleDialogOpen: () -> Unit,
) {
    Column(
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
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
            emptyContentAlignment = Alignment.TopCenter,
            header = {
                DatePickerCard(datePickerState = datePickerState) {
                    QuickViewDialogButtons(
                        isMealDialogEnabled = uiState.isMealExists,
                        onMealDialogOpen = onMealDialogOpen,
                        isScheduleDialogEnabled = uiState.isScheduleOrMemoExists,
                        onScheduleDialogOpen = onScheduleDialogOpen,
                    )
                }
            }
        )
    }
}

@Composable
private fun DatePickerCard(
    datePickerState: DailyDatePickerState,
    modifier: Modifier = Modifier,
    trailingContents: @Composable (() -> Unit)? = null,
) {
    ElevatedCard(
        modifier = modifier,
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
    ) {
        Column {
            DailyDatePicker(dailyDatePickerState = datePickerState)
            DateQuickNavigationButtons(
                datePickerState = datePickerState,
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                    .fillMaxWidth(),
                navigationElements = DateQuickNavigation.entries.filter {
                    it.daysToMove.absoluteValue <= 1
                }
            )
            trailingContents?.invoke()
        }
    }
}

@LightPreview
@Composable
private fun VerticalDailyMainScreenPreview() {
    val selectedDate = Date(2024, 1, 8)
    val uiState = MainUiState(
        userId = "",
        selectedDate = selectedDate,
        monthlyDataState = (1..3).map {
            DailyData(
                schoolCode = 1,
                date = Date(2024, 1, 7).plusDays(it),
                uiMeals = UiMeals(UiMeal(2024, 1, 10, "중식", previewUiMenus, previewUiNutrients)),
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
        selectedMealIndex = 0,
        isLoading = false,
        selectedSchool = School(
            name = "어떤 학교",
            schoolCode = -1,
        ),
        isMealDialogVisible = false,
        isScheduleDialogVisible = false,
        mainUiMode = MainUiMode.Daily,
    )
    val datePickerState = rememberDailyDatePickerState(
        initialDate = uiState.selectedDate,
        initialTextFieldValue = uiState.selectedDate.toTextFieldFormat(),
    )
    val mealPagerState = rememberPagerState { 3 }
    BlindarTheme {
        VerticalDailyMainScreen(
            uiState = uiState,
            mealPagerState = mealPagerState,
            datePickerState = datePickerState,
            onRefreshIconClick = {},
            onSettingsIconClick = {},
            onSchoolNameClick = {},
            onMealTimeClick = {},
            onNutrientButtonClick = {},
            onMemoButtonClick = {},
            onMealDialogOpen = {},
            onScheduleDialogOpen = {},
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface),
        )
    }
}