package com.example.domain

import com.practice.database.meal.entity.MealEntity
import com.practice.database.schedule.entity.ScheduleEntity
import kotlinx.collections.immutable.ImmutableList

data class MealScheduleModel(
    val year: Int,
    val month: Int,
    val meals: ImmutableList<MealEntity>,
    val schedules: ImmutableList<ScheduleEntity>,
)