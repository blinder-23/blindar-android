package com.practice.util.date.daytype

import com.hsk.ktx.date.Date
import com.hsk.ktx.date.DayOfWeek
import com.hsk.ktx.date.MonthDay

internal fun Date.toLunarDate() = KoreanLunarCalendar.getInstance().let {
    it.setSolarDate(year, month, dayOfMonth)
    MonthDay(it.lunarMonth, it.lunarDay)
}

private val names = listOf("일", "월", "화", "수", "목", "금", "토")

fun DayOfWeek.toKor(): String {
    return names[this.ordinal]
}