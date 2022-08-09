package com.practice.hanbitlunch.util

import java.time.LocalDate
import java.util.*

data class Date(
    val year: Int,
    val month: Int,
    val day: Int
) {
    constructor(localDate: LocalDate) : this(
        year = localDate.year,
        month = localDate.monthValue,
        day = localDate.dayOfMonth
    )

    companion object {}
}

fun Date.Companion.now(): Date {
    val current = Calendar.getInstance()
    val year = current.get(Calendar.YEAR)
    val month = current.get(Calendar.MONTH) + 1
    val day = current.get(Calendar.DATE)
    return Date(year, month, day)
}