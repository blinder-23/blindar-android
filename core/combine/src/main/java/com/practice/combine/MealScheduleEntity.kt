package com.practice.combine

import com.practice.meal.entity.MealEntity
import com.practice.schedule.entity.ScheduleEntity
import kotlinx.collections.immutable.ImmutableList

data class MealScheduleEntity(
    val year: Int,
    val month: Int,
    val meals: ImmutableList<MealEntity>,
    val schedules: ImmutableList<ScheduleEntity>,
)