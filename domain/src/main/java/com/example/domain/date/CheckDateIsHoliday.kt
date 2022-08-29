package com.example.domain.date

import java.time.DateTimeException
import java.time.LocalDate
import java.time.MonthDay

internal fun LocalDate.isHoliday(): Boolean {
    if (this == LocalDate.MAX || this == LocalDate.MIN) return false
    val monthDay = MonthDay.of(monthValue, dayOfMonth)
    // 2월 30일이 에러를 일으킨다.
    // 음력에는 존재하지만, 양력에는 존재하지 않기 때문이다.
    val monthDayToLunar = try {
        this.toLunarDate()
    } catch (e: DateTimeException) {
        return false
    }
    // 공휴일
    if (holidays.contains(monthDay) || lunarHolidays.contains(monthDayToLunar)) return true
    // 대체공휴일
    return this.isAlternativeHoliday()
}

private fun LocalDate.isAlternativeHoliday(): Boolean {
    return alternativeHolidays.contains(this)
}