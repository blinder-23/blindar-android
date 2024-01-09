package com.practice.main.daily.picker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hsk.ktx.date.Date
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
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        DailyDatePickerHeader(dailyDatePickerState.selectedDate)
        DailyDatePickerTextField(dailyDatePickerState)
    }
}

@Composable
private fun DailyDatePickerHeader(selectedDate: Date) {
    LabelLarge(text = stringResource(id = R.string.daily_date_picker_title))
    Spacer(modifier = Modifier.height(4.dp))
    HeadlineLarge(text = selectedDate.toHeadlineFormat())
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
        textColor = errorTextColor,
        modifier = modifier,
    )
}

@Composable
private fun Date.toHeadlineFormat(): String {
    return stringResource(
        id = R.string.daily_date_picker_headline_format,
        year,
        month,
        dayOfMonth,
        dayOfWeek.fullName,
    )
}

@Preview(showBackground = true, device = "spec:width=1280dp,height=800dp,dpi=480")
@Composable
private fun DailyDatePickerPreview() {
    val dailyDatePickerState = rememberDailyDatePickerState()
    BlindarTheme {
        Column(
            modifier = Modifier
                .width(700.dp)
                .background(MaterialTheme.colorScheme.surface),
        ) {
            DailyDatePicker(
                dailyDatePickerState = dailyDatePickerState,
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Button(onClick = { dailyDatePickerState.minusDays(7) }) {
                    Text("일주일 전")
                }
                Button(onClick = { dailyDatePickerState.minusDays(1) }) {
                    Text("어제")
                }
                Button(onClick = { dailyDatePickerState.setToday() }) {
                    Text("오늘")
                }
                Button(onClick = { dailyDatePickerState.plusDays(1) }) {
                    Text("내일")
                }
                Button(onClick = { dailyDatePickerState.plusDays(7) }) {
                    Text("일주일 뒤")
                }
            }
        }
    }
}