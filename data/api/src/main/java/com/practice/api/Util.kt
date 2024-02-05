package com.practice.api

import com.hsk.ktx.date.Date

/**
 * Add [hours] to this epoch value.
 */
private fun Long.plusHours(hours: Int) = this + 3600 * hours

fun Long.toEpochDate(hourDiff: Int): Date = Date.ofEpochDay(this.plusHours(hourDiff))