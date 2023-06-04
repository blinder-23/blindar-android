package com.practice.date

import com.hsk.ktx.date.Date
import com.hsk.ktx.date.MonthDay

internal val holidays = listOf(
    MonthDay(1, 1),
    MonthDay(3, 1),
    MonthDay(5, 5),
    MonthDay(6, 6),
    MonthDay(8, 15),
    // TODO: 개교기념일 추가
    MonthDay(10, 3),
    MonthDay(10, 9),
    MonthDay(12, 25)
)

internal val lunarHolidays = listOf(
    MonthDay(12, 31),
    MonthDay(1, 1),
    MonthDay(1, 2),
    MonthDay(8, 14),
    MonthDay(8, 15),
    MonthDay(8, 16),
)

internal val alternativeHolidays = listOf(
    Date(2020, 1, 27),
    Date(2021, 8, 16),
    Date(2021, 10, 4),
    Date(2021, 10, 11),
    Date(2022, 9, 12),
    Date(2022, 10, 10),
    Date(2023, 1, 24),
    Date(2024, 2, 12),
    Date(2024, 5, 6),
    Date(2025, 3, 3),
    Date(2025, 5, 6),
    Date(2025, 10, 8),
)