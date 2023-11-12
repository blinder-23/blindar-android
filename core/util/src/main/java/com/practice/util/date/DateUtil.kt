package com.practice.util.date

import com.hsk.ktx.date.Date

object DateUtil {
    fun today() = Date.now()

    fun toDateString(year: Int, month: Int, dayOfMonth: Int): String {
        val formattedYear = year.toString().padStart(4, '0')
        val formattedMonth = month.toString().padStart(2, '0')
        val formattedDayOfMonth = dayOfMonth.toString().padStart(2, '0')
        return "$formattedYear$formattedMonth$formattedDayOfMonth"
    }
}