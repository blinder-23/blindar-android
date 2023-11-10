package com.practice.combine

import com.practice.domain.Memo
import com.practice.domain.meal.Meal
import com.practice.domain.schedule.Schedule
import kotlinx.collections.immutable.ImmutableList

data class MonthlyData(
    val schoolCode: Int,
    val year: Int,
    val month: Int,
    val meals: ImmutableList<Meal>,
    val schedules: ImmutableList<Schedule>,
    val memos: ImmutableList<Memo>,
)