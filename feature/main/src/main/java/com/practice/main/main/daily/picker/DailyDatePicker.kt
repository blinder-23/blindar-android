package com.practice.main.main.daily.picker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.hsk.ktx.date.Date
import com.practice.designsystem.DarkPreview
import com.practice.designsystem.components.BodySmall
import com.practice.designsystem.components.HeadlineLarge
import com.practice.designsystem.components.LabelLarge
import com.practice.designsystem.components.LabelMedium
import com.practice.designsystem.theme.BlindarTheme
import com.practice.main.R

@Composable
fun DailyDatePicker(
    modifier: Modifier = Modifier,
    dailyDatePickerState: DailyDatePickerState = rememberDailyDatePickerState(),
) {
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        DailyDatePickerHeader(dailyDatePickerState.selectedDate)
        DailyDatePickerTextField(dailyDatePickerState)
    }
}

@Composable
private fun DailyDatePickerHeader(
    selectedDate: Date,
    modifier: Modifier = Modifier
) {
    val textColor = MaterialTheme.colorScheme.onSurface
    Column(
        modifier = modifier.semantics(mergeDescendants = true) {},
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        LabelLarge(
            text = stringResource(id = R.string.daily_date_picker_title),
            color = textColor,
        )
        HeadlineLarge(
            text = selectedDate.toHeadlineFormat(),
            color = textColor,
        )
    }
}

@Composable
private fun DailyDatePickerTextField(dailyDatePickerState: DailyDatePickerState) {
    OutlinedTextField(
        value = dailyDatePickerState.textFieldValue,
        onValueChange = dailyDatePickerState::onTextFieldUpdate,
        visualTransformation = remember { DateVisualTransformation() },
        isError = dailyDatePickerState.isError,
        supportingText = {
            DailyDatePickerSupportingText(
                isError = dailyDatePickerState.isError,
                selectedDate = dailyDatePickerState.selectedDate,
            )
        },
        label = {
            LabelMedium(text = stringResource(id = R.string.daily_date_picker_label))
        },
        placeholder = {
            BodySmall(text = stringResource(id = R.string.daily_date_picker_placeholder))
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
private fun DailyDatePickerSupportingText(
    isError: Boolean,
    selectedDate: Date,
    modifier: Modifier = Modifier
) {
    val errorText = if (isError) {
        stringResource(id = R.string.daily_date_picker_error)
    } else {
        selectedDate.toHeadlineFormat()
    }
    val errorTextColor =
        if (isError) MaterialTheme.colorScheme.error else Color.Transparent
    LabelMedium(
        text = errorText,
        color = errorTextColor,
        modifier = modifier.clearAndSetSemantics {
            contentDescription = if (isError) errorText else ""
        },
    )
}

@Composable
private fun Date.toHeadlineFormat(): String {
    return stringResource(
        id = R.string.daily_date_picker_headline_format,
        year,
        month,
        dayOfMonth,
        dayOfWeek.shortName,
    )
}

@DarkPreview
@Composable
private fun DailyDatePickerPreview() {
    val dailyDatePickerState = rememberDailyDatePickerState()
    BlindarTheme {
        DailyDatePicker(
            dailyDatePickerState = dailyDatePickerState,
            modifier = Modifier.background(MaterialTheme.colorScheme.surface),
        )
    }
}

@DarkPreview
@Composable
private fun DailyDatePickerPreview_error() {
    val dailyDatePickerState = rememberDailyDatePickerState(isError = true)
    BlindarTheme {
        DailyDatePicker(
            dailyDatePickerState = dailyDatePickerState,
            modifier = Modifier.background(MaterialTheme.colorScheme.surface),
        )
    }
}