package com.practice.util.date

import com.hsk.ktx.date.Date
import java.time.LocalDateTime
import java.time.ZoneOffset

object DateUtil {
    fun today() = Date.now()

    fun toDateString(year: Int, month: Int, dayOfMonth: Int): String {
        val formattedYear = year.toString().padStart(4, '0')
        val formattedMonth = month.toString().padStart(2, '0')
        val formattedDayOfMonth = dayOfMonth.toString().padStart(2, '0')
        return "$formattedYear$formattedMonth$formattedDayOfMonth"
    }

    fun getNextTimeOffsetInMillis(hour: Int, minute: Int, second: Int): Long {
        val now = LocalDateTime.now()
        val zone = ZoneOffset.systemDefault()
        val time = LocalDateTime.now().withHour(hour).withMinute(minute).withSecond(second)
        val nextTime = if (time.atZone(zone).toEpochSecond() <= now.atZone(zone).toEpochSecond()) {
            time.plusDays(1)
        } else {
            time
        }

        return nextTime.atZone(zone).toEpochSecond() - now.atZone(zone).toEpochSecond()
    }
}