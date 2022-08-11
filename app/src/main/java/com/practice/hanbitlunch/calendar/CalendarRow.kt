package com.practice.hanbitlunch.calendar

import java.time.DayOfWeek
import java.time.LocalDate

class CalendarRow private constructor(
    val dates: List<LocalDate>
) {

    /**
     * Returns a date object which corresponds to given week and day.
     *
     * All parameters are **1-indexed**.
     * For example, 1 represents the first week or Sunday respectively.
     */
    fun getDate(week: Int, day: Int): LocalDate {
        try {
            return dates[(week - 1) * 7 + (day - 1)]
        } catch (e: IndexOutOfBoundsException) {
            throw CalendarException("Week should be in [1, 5] and day in [1, 7] but actual: $week, $day.")
        }
    }

    companion object {
        fun getInstance(year: Int, month: Int): CalendarRow {
            val range = getCalendarRange(year, month)

            val dates = mutableListOf<LocalDate>()
            var currentDate = range.start
            while (range.contains(currentDate)) {
                dates.add(currentDate)
                currentDate = currentDate.plusDays(1)
            }
            return CalendarRow(dates)
        }
    }
}

class CalendarException(override val message: String?) : Exception(message)

private fun getCalendarRange(year: Int, month: Int) =
    getFirstDateOfCalendar(year, month).rangeTo(getLastDateOfCalendar(year, month))

private fun getFirstDateOfCalendar(year: Int, month: Int): LocalDate {
    var date = LocalDate.of(year, month, 1)
    while (date.dayOfWeek != DayOfWeek.SUNDAY) {
        date = date.minusDays(1)
    }
    return date
}

private fun getLastDateOfCalendar(year: Int, month: Int): LocalDate {
    var date = LocalDate.of(year, month, 1).plusMonths(1).minusDays(1)
    while (date.dayOfWeek != DayOfWeek.SATURDAY) {
        date = date.plusDays(1)
    }
    return date
}