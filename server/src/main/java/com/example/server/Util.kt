package com.example.server

import java.time.LocalDate

/**
 * Convert epoch second to epoch day.
 * Can be used with [LocalDate.ofEpochDay].
 */
private fun Long.toEpochDay(timeDiff: Int) = (this + 3600 * timeDiff) / 86400

fun Long.toEpochDate(timeDiff: Int): LocalDate = LocalDate.ofEpochDay(this.toEpochDay(timeDiff))