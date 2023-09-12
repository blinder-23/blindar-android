package com.practice.main.state

import com.hsk.ktx.date.Date
import com.practice.domain.schedule.Schedule
import com.practice.util.date.DateUtil
import kotlinx.collections.immutable.toPersistentList

data class DailyMealScheduleState(
    val schoolCode: Int,
    val date: Date,
    val mealUiState: MealUiState,
    val scheduleUiState: ScheduleUiState,
) : Comparable<DailyMealScheduleState> {
    override fun compareTo(other: DailyMealScheduleState): Int {
        return date.compareTo(other.date)
    }

    companion object {
        private const val EMPTY_SCHOOL_CODE = -1

        val sample = DailyMealScheduleState(
            schoolCode = EMPTY_SCHOOL_CODE,
            date = Date(2022, 10, 11),
            mealUiState = MealUiState(
                (1..6).map { Menu("식단 $it") }.toPersistentList()
            ),
            scheduleUiState = ScheduleUiState((1..3).map {
                Schedule(
                    schoolCode = EMPTY_SCHOOL_CODE,
                    year = 2022,
                    month = 10,
                    day = it,
                    id = it,
                    eventName = "제목 $it",
                    eventContent = "학사일정 $it",
                )
            }
                .toPersistentList()
            ),
        )
        val Empty = DailyMealScheduleState(
            schoolCode = EMPTY_SCHOOL_CODE,
            date = DateUtil.today(),
            mealUiState = MealUiState.EmptyMealState,
            scheduleUiState = ScheduleUiState.EmptyScheduleState,
        )
    }
}