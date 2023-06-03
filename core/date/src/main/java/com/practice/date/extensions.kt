package com.practice.date

import com.hsk.ktx.date.Date
import com.hsk.ktx.date.DayOfWeek
import com.hsk.ktx.date.MonthDay

internal fun Date.toLunarDate() = com.practice.date.KoreanLunarCalendar.getInstance().let {
    it.setSolarDate(year, month, dayOfMonth)
    MonthDay(it.lunarMonth, it.lunarDay)
}

private val names = listOf("일", "월", "화", "수", "목", "금", "토")

fun DayOfWeek.toKor(): String {
    return com.practice.date.names[this.ordinal]
}