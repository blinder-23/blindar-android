package com.example.domain

import com.example.domain.date.CalculateDayTypeUseCase
import com.example.domain.date.DayType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate

class CalculateDayTypeUseCaseTest {

    private val useCase = CalculateDayTypeUseCase()

    @Test
    fun weekday() {
        val date = LocalDate.of(2022, 8, 11)
        assertThat(useCase.get(date.monthValue, date)).isEqualTo(DayType.Weekday)
    }

    @Test
    fun `weekday over month`() {
        val date = LocalDate.of(2022, 8, 11)
        assertThat(useCase.get(9, date)).isEqualTo(DayType.WeekdayOverMonth)
    }

    @Test
    fun saturday() {
        val date = LocalDate.of(2022, 8, 6)
        assertThat(useCase.get(date.monthValue, date)).isEqualTo(DayType.Saturday)
    }

    @Test
    fun `saturday over month`() {
        val date = LocalDate.of(2022, 8, 6)
        assertThat(useCase.get(7, date)).isEqualTo(DayType.SaturdayOverMonth)
    }

    @Test
    fun sunday() {
        val date = LocalDate.of(2022, 8, 14)
        assertThat(useCase.get(date.monthValue, date)).isEqualTo(DayType.Holiday)
    }

    @Test
    fun holiday() {
        val date = LocalDate.of(2022, 8, 15)
        assertThat(useCase.get(date.monthValue, date)).isEqualTo(DayType.Holiday)
    }

    @Test
    fun `holiday over month`() {
        val date = LocalDate.of(2022, 10, 3)
        assertThat(useCase.get(11, date)).isEqualTo(DayType.HolidayOverMonth)
    }

    @Test
    fun `alternative holiday`() {
        val date = LocalDate.of(2022, 9, 12)
        assertThat(useCase.get(date.monthValue, date)).isEqualTo(DayType.Holiday)
    }

}