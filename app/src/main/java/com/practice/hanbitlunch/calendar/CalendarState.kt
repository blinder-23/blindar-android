package com.practice.hanbitlunch.calendar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import java.time.LocalDate
import java.time.Month

interface CalendarState {
    var year: Int
    var month: Int
    var selectedDate: LocalDate
}

fun CalendarState(
    year: Int,
    month: Int,
    selectedDate: LocalDate
): CalendarState = CalendarStateImpl(year, month, selectedDate)

@Composable
fun rememberCalendarState(): CalendarState {
    val now = LocalDate.now()
    return rememberCalendarState(
        year = now.year,
        month = now.monthValue,
        selectedDate = now
    )
}

@Composable
fun rememberCalendarState(
    year: Int,
    month: Int,
    selectedDate: LocalDate
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
            selectedDate = LocalDate.of(it[2], Month.of(it[3]), it[4]),
        )
    }
)

class CalendarStateImpl(
    year: Int,
    month: Int,
    selectedDate: LocalDate
) : CalendarState {
    private var _year by mutableStateOf(year)
    override var year: Int
        get() = _year
        set(value) {
            _year = value
        }

    private var _month by mutableStateOf(month)
    override var month: Int
        get() = _month
        set(value) {
            _month = value
        }

    private var _selectedDate by mutableStateOf(selectedDate)
    override var selectedDate: LocalDate
        get() = _selectedDate
        set(value) {
            _selectedDate = value
        }
}