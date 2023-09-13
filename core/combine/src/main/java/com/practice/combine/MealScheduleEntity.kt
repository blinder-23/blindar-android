package com.practice.combine

import com.practice.domain.meal.Meal
import com.practice.domain.schedule.Schedule
import kotlinx.collections.immutable.ImmutableList

// TODO: 이름 바꾸기 (MonthlyMealSchedule)
data class MealScheduleEntity(
    val schoolCode: Int,
    val year: Int,
    val month: Int,
    val meals: ImmutableList<Meal>,
    val schedules: ImmutableList<Schedule>,
)