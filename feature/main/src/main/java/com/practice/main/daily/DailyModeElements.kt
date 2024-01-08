package com.practice.main.daily

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerFormatter
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.practice.designsystem.LightPreview
import com.practice.designsystem.theme.BlindarTheme
import com.practice.main.previewMainUiState

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