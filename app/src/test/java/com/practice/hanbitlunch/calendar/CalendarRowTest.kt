package com.practice.hanbitlunch.calendar

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate

class CalendarRowTest {

    @Test
    fun test_2022_8() {
        val row = CalendarRow.getInstance(2022, 8)
        assertThat(row.getDate(1, 1)).isEqualTo(LocalDate.of(2022, 7, 31))
        assertThat(row.getDate(1, 7)).isEqualTo(LocalDate.of(2022, 8, 6))
        assertThat(row.getDate(5, 7)).isEqualTo(LocalDate.of(2022, 9, 3))
    }

    @Test
    fun test_2022_2() {
        val row = CalendarRow.getInstance(2022, 2)
        assertThat(row.getDate(1, 1)).isEqualTo(LocalDate.of(2022, 1, 30))
        assertThat(row.getDate(2, 5)).isEqualTo(LocalDate.of(2022, 2, 10))
    }

}