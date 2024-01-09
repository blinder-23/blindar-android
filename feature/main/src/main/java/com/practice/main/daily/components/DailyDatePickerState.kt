package com.practice.main.daily.components

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.mapSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.core.text.isDigitsOnly
import com.hsk.ktx.date.Date
import java.io.Serializable

@Composable
fun rememberDailyDatePickerState(
    initialDate: Date = Date.now(),
    initialTextFieldValue: String = Date.now().toTextFieldFormat(),
    isError: Boolean = false,
    onDateInput: (Date) -> Unit = {}
) = rememberSaveable(saver = DailyDatePickerState.Saver) {
    DailyDatePickerState(
        initialDate = initialDate,
        initialTextFieldValue = initialTextFieldValue,
        isError = isError,
        onDateInput = onDateInput,
    )
}

class DailyDatePickerState(
    initialDate: Date,
    initialTextFieldValue: String,
    isError: Boolean,
    private val onDateInput: (Date) -> Unit,
) {
    var selectedDate by mutableStateOf(initialDate)
        private set

    var textFieldValue by mutableStateOf(
        TextFieldValue(
            text = initialTextFieldValue,
            selection = TextRange(initialTextFieldValue.length),
        )
    )
        private set

    var isError by mutableStateOf(isError)
        private set

    private fun setSelected(date: Date) {
        selectedDate = date
        onTextFieldUpdate(date.toTextFieldValue(), setDate = false)
    }

    fun onTextFieldUpdate(value: TextFieldValue, setDate: Boolean = true) {
        val text = value.text
        if (!text.isDigitsOnly() || text.length > 8) return
        Log.d("DailyDatePickerState", "set text value to $value")
        textFieldValue = value
        if (text.length == 8) {
            text.parseToDate()?.let {
                if (it.isValid()) {
                    isError = false
                    if (setDate) {
                        setSelected(it)
                    }
                    onDateInput(it)
                } else {
                    isError = true
                }
            }
        } else { // value.length < 8
            isError = false
        }
    }

    fun plusDays(days: Int) {
        setSelected(selectedDate.plusDays(days))
    }

    fun setToday() {
        setSelected(Date.now())
    }

    fun minusDays(days: Int) {
        setSelected(selectedDate.minusDays(days))
    }

    private fun String.parseToDate(): Date? {
        val year = substring(0..3).toIntOrNull()
        val month = substring(4..5).toIntOrNull()
        val day = substring(6..7).toIntOrNull()
        Log.d(TAG, "$year, $month, $day")

        return if (year != null && month != null && day != null) {
            Date(year, month, day)
        } else {
            null
        }
    }

    companion object {
        private const val TAG = "DailyDatePickerState"

        private const val yearKey = "year"
        private const val monthKey = "month"
        private const val dayOfMonthKey = "day_of_month"
        private const val textFieldKey = "text"
        private const val isErrorKey = "is_error"
        private const val lambdaKey = "lambda"

        val Saver = mapSaver(
            save = {
                val (year, month, day) = it.selectedDate
                mapOf(
                    yearKey to year,
                    monthKey to month,
                    dayOfMonthKey to day,
                    textFieldKey to it.textFieldValue.text,
                    isErrorKey to it.isError,
                    lambdaKey to DateInputHolder(it.onDateInput),
                )
            },
            restore = {
                val year = it[yearKey] as Int
                val month = it[monthKey] as Int
                val day = it[dayOfMonthKey] as Int
                DailyDatePickerState(
                    initialDate = Date(year, month, day),
                    initialTextFieldValue = it[textFieldKey] as String,
                    isError = it[isErrorKey] as Boolean,
                    onDateInput = (it[lambdaKey] as DateInputHolder).action,
                )
            },
        )
    }
}

private class DateInputHolder(val action: (Date) -> Unit) : Serializable {
    operator fun invoke(date: Date) {
        action(date)
    }
}

fun Date.toTextFieldValue() = this.toTextFieldFormat().toTextFieldValue()

fun Date.toTextFieldFormat(): String {
    val monthPadZero = month.toString().padStart(2, '0')
    val dayOfMonthPadZero = dayOfMonth.toString().padStart(2, '0')
    return "$year$monthPadZero$dayOfMonthPadZero"
}

fun String.toTextFieldValue() = TextFieldValue(
    text = this,
    selection = TextRange(this.length),
)