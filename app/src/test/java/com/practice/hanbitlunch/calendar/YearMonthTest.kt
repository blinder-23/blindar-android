package com.practice.hanbitlunch.calendar

import com.practice.hanbitlunch.calendar.core.YearMonth
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class YearMonthTest {

    @Test
    fun testMonthDiff_sameYear() {
        val yearMonth1 = YearMonth(2023, 2)
        val yearMonth2 = YearMonth(2023, 9)
        assertThat(yearMonth1.monthDiff(yearMonth2)).isEqualTo(-7)
        assertThat(yearMonth2.monthDiff(yearMonth1)).isEqualTo(7)
    }

    @Test
    fun testMonthDiff_differentYear() {
        val yearMonth1 = YearMonth(2021, 10)
        val yearMonth2 = YearMonth(2023, 2)
        assertThat(yearMonth1.monthDiff(yearMonth2)).isEqualTo(-16)
    }

    @Test
    fun testMonthDiff_sameYearMonth() {
        val yearMonth = YearMonth(2023, 2)
        assertThat(yearMonth.monthDiff(yearMonth)).isEqualTo(0)
    }

}