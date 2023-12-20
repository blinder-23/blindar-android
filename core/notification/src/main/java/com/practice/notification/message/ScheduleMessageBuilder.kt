package com.practice.notification.message

import com.hsk.ktx.date.Date

internal class ScheduleMessageBuilder(private val date: Date, private val contents: List<String>) {
    val title: String
        get() = "${date.month}월 ${date.dayOfMonth}일 일정"

    val body: String
        get() = contents.joinToString("\n")
}