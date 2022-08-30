package com.practice.hanbitlunch.calendar

data class YearMonth(
    val year: Int,
    val month: Int
)

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