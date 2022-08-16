package com.example.domain.date

import java.time.LocalDate
import java.time.MonthDay

internal val holidays = listOf(
    MonthDay.of(1, 1),
    MonthDay.of(3, 1),
    MonthDay.of(5, 5),
    MonthDay.of(8, 15),
    // TODO: 개교기념일 추가
    MonthDay.of(10, 3),
    MonthDay.of(10, 9),
    MonthDay.of(12, 25)
)

internal val lunarHolidays = listOf(
    MonthDay.of(12, 31),
    MonthDay.of(1, 1),
    MonthDay.of(1, 2),
    MonthDay.of(8, 14),
    MonthDay.of(8, 15),
    MonthDay.of(8, 16),
)

internal val alternativeHolidays = listOf(
    LocalDate.of(2020, 1, 27),
    LocalDate.of(2021, 8, 16),
    LocalDate.of(2021, 10, 4),
    LocalDate.of(2021, 10, 11),
    LocalDate.of(2022, 9, 12),
    LocalDate.of(2022, 10, 10),
    LocalDate.of(2023, 1, 24),
    LocalDate.of(2024, 2, 12),
    LocalDate.of(2024, 5, 6),
    LocalDate.of(2025, 3, 3),
    LocalDate.of(2025, 5, 6),
    LocalDate.of(2025, 10, 8),
)