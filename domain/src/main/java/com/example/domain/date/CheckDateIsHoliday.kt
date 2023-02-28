package com.example.domain.date

import com.hsk.ktx.date.Date
import com.hsk.ktx.date.MonthDay

fun Date.isHoliday(): Boolean {
    if (this == Date.MAX || this == Date.MIN) return false
    val monthDay = MonthDay(month, dayOfMonth)
    // 2월 30일이 에러를 일으킨다.
    // 음력에는 존재하지만, 양력에는 존재하지 않기 때문이다.
    val monthDayToLunar = try {
        this.toLunarDate()
    } catch (e: Exception) {
        return false
    }
    // 공휴일
    if (holidays.contains(monthDay) || lunarHolidays.contains(monthDayToLunar)) return true
    // 대체공휴일
    return this.isAlternativeHoliday()
}

private fun Date.isAlternativeHoliday(): Boolean {
    return alternativeHolidays.contains(this)
}