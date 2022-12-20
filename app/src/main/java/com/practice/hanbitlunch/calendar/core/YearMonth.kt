package com.practice.hanbitlunch.calendar.core

data class YearMonth(
    val year: Int,
    val month: Int
) : Comparable<YearMonth> {

    override fun toString() = "$year$month"

    override fun compareTo(other: YearMonth): Int {
        return if (year != other.year) year.compareTo(other.year) else month.compareTo(other.month)
    }
}

fun YearMonth.offset(offset: Int): YearMonth {
    var year = this.year
    var month = this.month
    month += offset
    if (month <= 0) {
        while (month !in 1..12) {
            year--
            month += 12
        }
    } else if (month > 12) {
        year += month / 12
        month %= 12
    }
    return YearMonth(year, month)
}