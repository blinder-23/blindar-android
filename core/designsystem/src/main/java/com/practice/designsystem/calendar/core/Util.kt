package com.practice.designsystem.calendar.core

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.hsk.ktx.date.Date
import com.practice.util.date.daytype.isHoliday

val Date.clickLabel: String
    get() = "${month}월 ${dayOfMonth}일"

val Date.yearMonth: YearMonth
    get() = YearMonth(year, month)

fun YearMonth.getFirstWeekday(): Date {
    var date = Date(year, month, 1)
    while (month == date.month) {
        if (!date.isHoliday()) {
            return date
        }
        date = date.plusDays(1)
    }
    throw IllegalStateException("$year-$month doesn't have any weekday.")
}

fun DrawScope.drawUnderline(
    color: Color,
    strokeWidth: Float,
) {
    val (width, height) = this.size
    drawLine(
        color = color,
        start = Offset(0f, height),
        end = Offset(width, height),
        strokeWidth = strokeWidth,
    )
}