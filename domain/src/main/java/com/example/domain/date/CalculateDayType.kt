package com.example.domain.date

import java.time.DayOfWeek
import java.time.LocalDate

fun LocalDate.calculateDayType(currentMonth: Int): DayType {
    return if (this == LocalDate.MAX || this == LocalDate.MIN) {
        DayType.Weekday
    } else if (currentMonth == monthValue) {
        if (isHoliday()) {
            DayType.Holiday
        } else {
            when (dayOfWeek) {
                DayOfWeek.SUNDAY -> DayType.Holiday
                DayOfWeek.SATURDAY -> DayType.Saturday
                else -> DayType.Weekday
            }
        }
    } else {
        if (isHoliday()) {
            DayType.HolidayOverMonth
        } else {
            when (dayOfWeek) {
                DayOfWeek.SUNDAY -> DayType.HolidayOverMonth
                DayOfWeek.SATURDAY -> DayType.SaturdayOverMonth
                else -> DayType.WeekdayOverMonth
            }
        }
    }
}
