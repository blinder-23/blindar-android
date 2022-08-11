package com.example.domain.date

import java.time.DayOfWeek
import java.time.LocalDate

class CalculateDayTypeUseCase {

    fun get(currentMonth: Int, date: LocalDate): DayType {
        return if (currentMonth == date.monthValue) {
            if (date.isHoliday()) {
                DayType.Holiday
            } else {
                when (date.dayOfWeek) {
                    DayOfWeek.SUNDAY -> DayType.Holiday
                    DayOfWeek.SATURDAY -> DayType.Saturday
                    else -> DayType.Weekday
                }
            }
        } else {
            if (date.isHoliday()) {
                DayType.HolidayOverMonth
            } else {
                when (date.dayOfWeek) {
                    DayOfWeek.SUNDAY -> DayType.HolidayOverMonth
                    DayOfWeek.SATURDAY -> DayType.SaturdayOverMonth
                    else -> DayType.WeekdayOverMonth
                }
            }
        }
    }
}