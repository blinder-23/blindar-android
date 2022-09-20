package com.example.server

import java.time.LocalDate

/**
 * Convert epoch second to epoch day.
 * Can be used with [LocalDate.ofEpochDay].
 */
fun Long.toEpochDay() = this / 86400

fun Long.toEpochDate() = LocalDate.ofEpochDay(this.toEpochDay())