package com.practice.combine

import com.practice.domain.Memo
import com.practice.domain.meal.Meal
import com.practice.domain.schedule.Schedule

object TestUtil {
    const val userId = "abc123"
    const val schoolCode = 900
    fun createMealEntity(
        year: Int = 2022,
        month: Int = 8,
        day: Int = 1,
    ) = Meal(
        schoolCode = schoolCode,
        year = year,
        month = month,
        day = day,
        menus = emptyList(),
        origins = emptyList(),
        calorie = 100.0,
        nutrients = emptyList(),
        mealTime = "중식",
    )

    fun createScheduleEntity(
        year: Int = 2022,
        month: Int = 8,
        day: Int = 1,
        eventName: String = "",
        eventContent: String = ""
    ) = Schedule(
        schoolCode = schoolCode,
        id = 1,
        year = year,
        month = month,
        day = day,
        eventName = eventName,
        eventContent = eventContent
    )

    fun createMemo(
        id: String,
        year: Int = 2022,
        month: Int = 8,
        day: Int = 1,
        content: String = "",
    ) = Memo(
        id = id,
        userId = userId,
        year = year,
        month = month,
        day = day,
        content = content,
        isSavedOnRemote = false,
    )
}