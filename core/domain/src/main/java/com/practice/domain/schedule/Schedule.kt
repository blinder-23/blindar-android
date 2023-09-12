package com.practice.domain.schedule

data class Schedule(
    val schoolCode: Int,
    val id: Int,
    val year: Int,
    val month: Int,
    val day: Int,
    val eventName: String,
    val eventContent: String
)