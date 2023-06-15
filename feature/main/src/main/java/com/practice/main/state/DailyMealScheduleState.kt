package com.practice.main.state

import com.hsk.ktx.date.Date
import com.practice.util.date.DateUtil
import kotlinx.collections.immutable.toPersistentList

data class DailyMealScheduleState(
    val date: Date,
    val mealUiState: MealUiState,
    val scheduleUiState: ScheduleUiState,
) : Comparable<DailyMealScheduleState> {
    override fun compareTo(other: DailyMealScheduleState): Int {
        return date.compareTo(other.date)
    }

    companion object {
        val sample = DailyMealScheduleState(
            date = Date(2022, 10, 11),
            mealUiState = MealUiState(
                (1..6).map { Menu("식단 $it") }.toPersistentList()
            ),
            scheduleUiState = ScheduleUiState((1..3).map { Schedule("제목 $it", "학사일정 $it") }
                .toPersistentList()
            ),
        )
        val Empty = DailyMealScheduleState(
            date = DateUtil.today(),
            mealUiState = MealUiState.EmptyMealState,
            scheduleUiState = ScheduleUiState.EmptyScheduleState,
        )
    }
}