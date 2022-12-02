package com.practice.hanbitlunch.calendar

import com.hsk.ktx.date.Date
import com.hsk.ktx.date.DayOfWeek

class CalendarPage private constructor(
    val year: Int,
    val month: Int,
    val weeks: List<Week>
) {

    /**
     * Returns a date object which corresponds to given week and day.
     *
     * All parameters are **1-indexed**.
     * For example, 1 represents the first week or Sunday respectively.
     */
    fun getDate(week: Int, day: Int): Date {
        try {
            return weeks[(week - 1)][day - 1]
        } catch (e: IndexOutOfBoundsException) {
            throw CalendarException("Week should be in [1, 5] and day in [1, 7] but actual: $week, $day.")
        }
    }

    inline fun forEachWeeks(action: (Week) -> Unit) {
        weeks.forEach { action(it) }
    }

    companion object {
        fun getInstance(yearMonth: YearMonth) = with(yearMonth) {
            getInstance(year, month)
        }

        fun getInstance(year: Int, month: Int): CalendarPage {
            val weeks = makeWeeks(getCalendarRange(year, month))
            return CalendarPage(year, month, weeks)
        }

        private fun makeWeeks(range: ClosedRange<Date>): List<Week> {
            val weeks = mutableListOf<Week>()
            val dates = mutableListOf<Date>()

            var currentDate = range.start
            while (range.contains(currentDate)) {
                dates.add(currentDate)
                currentDate = currentDate.plusDays(1)

                if (dates.size == 7) {
                    // dates를 그대로 넣을 경우, reference 참조에 의해
                    // 모든 week의 dates가 같은 객체를 참조한다.
                    weeks.add(Week.create(dates))
                    dates.clear()
                }
            }
            if (dates.isNotEmpty()) {
                weeks.add(Week.create(dates))
            }
            return weeks
        }
    }
}

class Week private constructor(val dates: List<Date>) {

    companion object {
        fun create(dates: List<Date>): Week {
            return Week(dates.toList())
        }
    }

    operator fun get(index: Int) = dates[index]

    inline fun forEach(action: (Date) -> Unit) {
        dates.forEach { action(it) }
    }
}

class CalendarException(override val message: String?) : Exception(message)

private fun getCalendarRange(year: Int, month: Int) =
    getFirstDateOfCalendar(year, month).rangeTo(getLastDateOfCalendar(year, month))

private fun getFirstDateOfCalendar(year: Int, month: Int): Date {
    var date = Date(year, month, 1)
    while (date.dayOfWeek != DayOfWeek.SUNDAY) {
        date = date.minusDays(1)
    }
    return date
}

private fun getLastDateOfCalendar(year: Int, month: Int): Date {
    var date = Date(year, month, 1).plusMonths(1).minusDays(1)
    while (date.dayOfWeek != DayOfWeek.SATURDAY) {
        date = date.plusDays(1)
    }
    return date
}