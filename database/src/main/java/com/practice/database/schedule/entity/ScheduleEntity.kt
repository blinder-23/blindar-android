package com.practice.database.schedule.entity

data class ScheduleEntity(
    val schoolCode: Int,
    val year: Int,
    val month: Int,
    val day: Int,
    val eventName: String,
    val eventContent: String
)