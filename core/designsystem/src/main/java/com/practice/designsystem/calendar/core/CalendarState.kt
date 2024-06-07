package com.practice.designsystem.calendar.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.hsk.ktx.date.Date

interface CalendarState {
    var year: Int
    var month: Int
    var selectedDate: Date
}

fun CalendarState(
    year: Int,
    month: Int,
    selectedDate: Date
): CalendarState = CalendarStateImpl(year, month, selectedDate)

val CalendarState.yearMonth: YearMonth
    get() = YearMonth(year, month)

@Composable
fun rememberCalendarState(): CalendarState {
    val now = Date.now()
    return rememberCalendarState(
        year = now.year,
        month = now.month,
        selectedDate = now
    )
}

@Composable
fun rememberCalendarState(
    year: Int,
    month: Int,
    selectedDate: Date
) = rememberSaveable(year, month, selectedDate, saver = CalendarStateSaver) {
    CalendarState(year, month, selectedDate)
}

private val CalendarStateSaver = listSaver(
    save = {
        listOf(
            it.year,
            it.month,
            it.selectedDate.year,
            it.selectedDate.month,
            it.selectedDate.dayOfMonth
        )
    },
    restore = {
        CalendarState(
            year = it[0],
            month = it[1],
            selectedDate = Date(it[2], it[3], it[4]),
        )
    }
)

class CalendarStateImpl(
    year: Int,
    month: Int,
    selectedDate: Date
) : CalendarState {
    private var _year by mutableIntStateOf(year)
    override var year: Int
        get() = _year
        set(value) {
            _year = value
        }

    private var _month by mutableIntStateOf(month)
    override var month: Int
        get() = _month
        set(value) {
            _month = value
        }

    private var _selectedDate by mutableStateOf(selectedDate)
    override var selectedDate: Date
        get() = _selectedDate
        set(value) {
            _selectedDate = value
        }
}