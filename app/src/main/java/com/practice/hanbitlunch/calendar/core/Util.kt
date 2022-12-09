package com.practice.hanbitlunch.calendar.core

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.hsk.ktx.date.Date

val Date.clickLabel: String
    get() = "${month}월 ${dayOfMonth}일"

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