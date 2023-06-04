package com.practice.combine

import com.practice.meal.entity.MealEntity
import com.practice.schedule.entity.ScheduleEntity

object TestUtil {
    fun createMealEntity(
        year: Int = 2022,
        month: Int = 8,
        day: Int = 1,
    ) = MealEntity(
        schoolCode = 900,
        year = year,
        month = month,
        day = day,
        menus = emptyList(),
        origins = emptyList(),
        calorie = 100.0,
        nutrients = emptyList(),
    )

    fun createScheduleEntity(
        year: Int = 2022,
        month: Int = 8,
        day: Int = 1,
        eventName: String = "",
        eventContent: String = ""
    ) = ScheduleEntity(
        id = 1,
        year = year,
        month = month,
        day = day,
        eventName = eventName,
        eventContent = eventContent
    )
}