package com.example.domain.date

import java.time.LocalDate
import java.time.MonthDay

internal fun LocalDate.isHoliday(): Boolean {
    val monthDay = MonthDay.of(monthValue, dayOfMonth)
    val monthDayToLunar = this.toLunarDate()
    // 공휴일
    if (holidays.contains(monthDay) || lunarHolidays.contains(monthDayToLunar)) return true
    // 대체공휴일
    return this.isAlternativeHoliday()
}

private fun LocalDate.isAlternativeHoliday(): Boolean {
    return alternativeHolidays.contains(this)
}