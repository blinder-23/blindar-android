package com.practice.schedule.entity

data class ScheduleEntity(
    val id: Int,
    val year: Int,
    val month: Int,
    val day: Int,
    val eventName: String,
    val eventContent: String
)