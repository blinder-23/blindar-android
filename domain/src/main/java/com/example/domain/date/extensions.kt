package com.example.domain.date

import java.time.LocalDate
import java.time.MonthDay

internal fun LocalDate.toLunarDate() = KoreanLunarCalendar.getInstance().let {
    it.setSolarDate(year, monthValue, dayOfMonth)
    MonthDay.of(it.lunarMonth, it.lunarDay)
}