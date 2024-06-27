package com.practice.combine

import com.practice.domain.Memo
import com.practice.domain.meal.Meal
import com.practice.domain.schedule.Schedule
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class MonthlyData(
    val schoolCode: Int,
    val year: Int,
    val month: Int,
    val meals: ImmutableList<Meal>,
    val schedules: ImmutableList<Schedule>,
    val memos: ImmutableList<Memo>,
) {
    companion object {
        val Empty = MonthlyData(
            schoolCode = 0,
            year = 0,
            month = 0,
            meals = persistentListOf(),
            schedules = persistentListOf(),
            memos = persistentListOf(),
        )
    }
}