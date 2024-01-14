package com.practice.main.daily

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.practice.main.MainScreenContents
import com.practice.main.MainScreenTopBar
import com.practice.main.R
import com.practice.main.daily.components.DateQuickNavigation
import com.practice.main.daily.components.DateQuickNavigationButtons
import com.practice.main.daily.picker.DailyDatePicker
import com.practice.main.daily.picker.DailyDatePickerState
import com.practice.main.daily.picker.rememberDailyDatePickerState
import com.practice.main.daily.picker.toTextFieldFormat
import com.practice.main.previewMemos
import com.practice.main.previewMenus
import com.practice.main.previewNutrients
import com.practice.main.previewSchedules
import com.practice.main.state.DailyData
import com.practice.main.state.MainUiMode
import com.practice.main.state.MainUiState
import com.practice.main.state.UiMeal
import com.practice.main.state.UiMeals
import com.practice.main.state.UiMemos
import com.practice.main.state.UiSchedules
import kotlin.math.absoluteValue

@Composable
fun VerticalDailyMainScreen(
    uiState: MainUiState,
    datePickerState: DailyDatePickerState,
    mealColumns: Int,
    onRefreshIconClick: () -> Unit,
    onSettingsIconClick: () -> Unit,
    onSchoolNameClick: () -> Unit,
    onMealTimeClick: (Int) -> Unit,
    onNutrientPopupOpen: () -> Unit,
    onMemoPopupOpen: () -> Unit,
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
            uiState = uiState,
            mealColumns = mealColumns,
            onMealTimeClick = onMealTimeClick,
            onNutrientPopupOpen = onNutrientPopupOpen,
            onMemoPopupOpen = onMemoPopupOpen
        )
    }
}

@Composable
private fun VerticalDailyMainScreenContents(
    datePickerState: DailyDatePickerState,
    uiState: MainUiState,
    mealColumns: Int,
    onMealTimeClick: (Int) -> Unit,
    onNutrientPopupOpen: () -> Unit,
    onMemoPopupOpen: () -> Unit,
) {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        MainScreenContents(
            uiMeals = uiState.selectedDateDataState.uiMeals,
            memoPopupElements = uiState.selectedDateDataState.memoPopupElements,
            selectedMealIndex = uiState.selectedMealIndex,
            onMealTimeClick = onMealTimeClick,
            mealColumns = mealColumns,
            onNutrientPopupOpen = onNutrientPopupOpen,
            onMemoPopupOpen = onMemoPopupOpen,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            emptyContentAlignment = Alignment.TopCenter,
            header = {
                DatePickerCard(
                    datePickerState = datePickerState,
                )
            }
        )
    }
}

@Composable
private fun DatePickerCard(
    datePickerState: DailyDatePickerState,
    modifier: Modifier = Modifier,
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
        }
    }
}

@LightPreview
@Composable
private fun VerticalDailyMainScreenPreview() {
    val selectedDate = Date(2024, 1, 8)
    val uiState = MainUiState(
        userId = "",
        year = 2024,
        month = 1,
        selectedDate = selectedDate,
        monthlyDataState = (1..3).map {
            DailyData(
                schoolCode = 1,
                date = Date(2024, 1, 7).plusDays(it),
                uiMeals = UiMeals(UiMeal(2024, 1, 10, "중식", previewMenus, previewNutrients)),
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
        isNutrientPopupVisible = false,
        isMemoPopupVisible = false,
        mainUiMode = MainUiMode.DAILY,
    )
    val datePickerState = rememberDailyDatePickerState(
        initialDate = uiState.selectedDate,
        initialTextFieldValue = uiState.selectedDate.toTextFieldFormat(),
        onDateInput = { },
    )
    BlindarTheme {
        VerticalDailyMainScreen(
            uiState = uiState,
            datePickerState = datePickerState,
            mealColumns = 2,
            onRefreshIconClick = {},
            onSettingsIconClick = {},
            onSchoolNameClick = {},
            onMealTimeClick = {},
            onNutrientPopupOpen = {},
            onMemoPopupOpen = {},
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface),
        )
    }
}