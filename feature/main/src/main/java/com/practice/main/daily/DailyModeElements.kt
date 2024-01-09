package com.practice.main.daily

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerFormatter
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.practice.designsystem.LightAndDarkPreview
import com.practice.designsystem.LightPreview
import com.practice.designsystem.components.BodySmall
import com.practice.designsystem.theme.BlindarTheme
import com.practice.main.daily.components.DailyDatePickerState
import com.practice.main.daily.components.rememberDailyDatePickerState
import com.practice.main.previewMainUiState
import kotlin.math.absoluteValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DailyModeDatePicker(
    datePickerState: DatePickerState,
    onSelectDate: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    DisposableEffect(key1 = datePickerState.selectedDateMillis) {
        onDispose {
            datePickerState.selectedDateMillis?.let {
                onSelectDate(it)
            }
        }
    }

    Column(modifier = modifier) {
        DatePicker(
            state = datePickerState,
            dateFormatter = rememberDatePickerFormatter(),
            showModeToggle = false,
        )
    }
}

@Composable
internal fun DateQuickNavigationButtons(
    datePickerState: DailyDatePickerState,
    modifier: Modifier = Modifier,
    navigationElements: Collection<DateQuickNavigation> = DateQuickNavigation.entries,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        navigationElements.forEach {
            DateQuickNavigationButton(
                datePickerState = datePickerState,
                quickNavigation = it,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
internal fun DateQuickNavigationButton(
    datePickerState: DailyDatePickerState,
    quickNavigation: DateQuickNavigation,
    modifier: Modifier = Modifier,
) {
    val backgroundColor = MaterialTheme.colorScheme.primary
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(backgroundColor)
            .clickable(onClickLabel = stringResource(id = quickNavigation.descriptionId)) {
                if (quickNavigation == DateQuickNavigation.TODAY) {
                    datePickerState.setToday()
                } else if (quickNavigation.daysToMove > 0) {
                    datePickerState.plusDays(quickNavigation.daysToMove)
                } else if (quickNavigation.daysToMove < 0) {
                    datePickerState.minusDays(quickNavigation.daysToMove.absoluteValue)
                }
            }
            .padding(12.dp)
    ) {
        BodySmall(
            text = stringResource(id = quickNavigation.nameId),
            modifier = Modifier.align(Alignment.Center),
            textColor = contentColorFor(backgroundColor = backgroundColor),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun rememberDatePickerFormatter() = remember {
    DatePickerFormatter(
        yearSelectionSkeleton = "yMMMd",                // 2024년 1월 8일
        selectedDateSkeleton = "yMMMdEEE",              // 2024년 1월 8일 (월)
        selectedDateDescriptionSkeleton = "yMMMdEEEE",  // 2024년 1월 8일 월요일
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@LightPreview
@Composable
private fun DailyModeDatePickerPreview() {
    val datePickerState = rememberDailyModeDatePickerState(previewMainUiState())
    var selectedMillis by remember { mutableLongStateOf(0L) }

    BlindarTheme {
        Column(modifier = Modifier.fillMaxWidth()) {
            DailyModeDatePicker(
                datePickerState = datePickerState,
                onSelectDate = { selectedMillis = it },
                modifier = Modifier.fillMaxWidth(),
            )
            Text(
                text = selectedMillis.toString(),
            )
        }
    }
}

@LightAndDarkPreview
@Composable
private fun DateQuickNavigationButtonsPreview() {
    val datePickerState = rememberDailyDatePickerState()
    BlindarTheme {
        DateQuickNavigationButtons(
            datePickerState = datePickerState,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}