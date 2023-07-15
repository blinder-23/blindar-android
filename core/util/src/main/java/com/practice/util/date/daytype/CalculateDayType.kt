package com.practice.util.date.daytype

import com.hsk.ktx.date.Date
import com.hsk.ktx.date.DayOfWeek

fun Date.calculateDayType(currentMonth: Int): DayType {
    return if (this == Date.MAX || this == Date.MIN) {
        DayType.Weekday
    } else if (currentMonth == month) {
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
