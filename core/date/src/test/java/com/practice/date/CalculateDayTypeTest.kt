package com.practice.date

import com.hsk.ktx.date.Date
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CalculateDayTypeUseCaseTest {

    @Test
    fun weekday() {
        val date = Date(2022, 8, 11)
        assertThat(date.calculateDayType(date.month)).isEqualTo(com.practice.date.DayType.Weekday)
    }

    @Test
    fun `weekday over month`() {
        val date = Date(2022, 8, 11)
        assertThat(date.calculateDayType(9)).isEqualTo(com.practice.date.DayType.WeekdayOverMonth)
    }

    @Test
    fun saturday() {
        val date = Date(2022, 8, 6)
        assertThat(date.calculateDayType(date.month)).isEqualTo(com.practice.date.DayType.Saturday)
    }

    @Test
    fun `saturday over month`() {
        val date = Date(2022, 8, 6)
        assertThat(date.calculateDayType(7)).isEqualTo(com.practice.date.DayType.SaturdayOverMonth)
    }

    @Test
    fun sunday() {
        val date = Date(2022, 8, 14)
        assertThat(date.calculateDayType(date.month)).isEqualTo(com.practice.date.DayType.Holiday)
    }

    @Test
    fun holiday() {
        val date = Date(2022, 8, 15)
        assertThat(date.calculateDayType(date.month)).isEqualTo(com.practice.date.DayType.Holiday)
    }

    @Test
    fun `holiday over month`() {
        val date = Date(2022, 10, 3)
        assertThat(date.calculateDayType(11)).isEqualTo(com.practice.date.DayType.HolidayOverMonth)
    }

    @Test
    fun `alternative holiday`() {
        val date = Date(2022, 9, 12)
        assertThat(date.calculateDayType(date.month)).isEqualTo(com.practice.date.DayType.Holiday)
    }

}