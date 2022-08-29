package com.example.domain

import com.example.domain.date.DayType
import com.example.domain.date.calculateDayType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate

class CalculateDayTypeUseCaseTest {

    @Test
    fun weekday() {
        val date = LocalDate.of(2022, 8, 11)
        assertThat(date.calculateDayType(date.monthValue)).isEqualTo(DayType.Weekday)
    }

    @Test
    fun `weekday over month`() {
        val date = LocalDate.of(2022, 8, 11)
        assertThat(date.calculateDayType(9)).isEqualTo(DayType.WeekdayOverMonth)
    }

    @Test
    fun saturday() {
        val date = LocalDate.of(2022, 8, 6)
        assertThat(date.calculateDayType(date.monthValue)).isEqualTo(DayType.Saturday)
    }

    @Test
    fun `saturday over month`() {
        val date = LocalDate.of(2022, 8, 6)
        assertThat(date.calculateDayType(7)).isEqualTo(DayType.SaturdayOverMonth)
    }

    @Test
    fun sunday() {
        val date = LocalDate.of(2022, 8, 14)
        assertThat(date.calculateDayType(date.monthValue)).isEqualTo(DayType.Holiday)
    }

    @Test
    fun holiday() {
        val date = LocalDate.of(2022, 8, 15)
        assertThat(date.calculateDayType(date.monthValue)).isEqualTo(DayType.Holiday)
    }

    @Test
    fun `holiday over month`() {
        val date = LocalDate.of(2022, 10, 3)
        assertThat(date.calculateDayType(11)).isEqualTo(DayType.HolidayOverMonth)
    }

    @Test
    fun `alternative holiday`() {
        val date = LocalDate.of(2022, 9, 12)
        assertThat(date.calculateDayType(date.monthValue)).isEqualTo(DayType.Holiday)
    }

}