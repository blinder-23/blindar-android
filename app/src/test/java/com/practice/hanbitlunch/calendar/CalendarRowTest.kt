package com.practice.hanbitlunch.calendar

import com.practice.hanbitlunch.util.Date
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CalendarRowTest {

    @Test
    fun test_2022_8() {
        val row = CalendarRow.get(2022, 8)
        assertThat(row.get(1, 1)).isEqualTo(Date(2022, 7, 31))
        assertThat(row.get(1, 7)).isEqualTo(Date(2022, 8, 6))
        assertThat(row.get(5, 7)).isEqualTo(Date(2022, 9, 3))
    }

    @Test
    fun test_2022_2() {
        val row = CalendarRow.get(2022, 2)
        assertThat(row.get(1, 1)).isEqualTo(Date(2022, 1, 30))
        assertThat(row.get(2, 5)).isEqualTo(Date(2022, 2, 10))
    }

}