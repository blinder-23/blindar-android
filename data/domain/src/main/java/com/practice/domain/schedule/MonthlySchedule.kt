package com.practice.domain.schedule

data class MonthlySchedule(
    val schoolCode: Int,
    val year: Int,
    val month: Int,
    val schedules: List<Schedule>,
)
