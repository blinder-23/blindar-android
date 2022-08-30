package com.example.domain.date

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.MonthDay

internal fun LocalDate.toLunarDate() = KoreanLunarCalendar.getInstance().let {
    it.setSolarDate(year, monthValue, dayOfMonth)
    MonthDay.of(it.lunarMonth, it.lunarDay)
}

private val names = listOf("월", "화", "수", "목", "금", "토", "일")

fun DayOfWeek.toKor(): String {
    return names[this.ordinal]
}