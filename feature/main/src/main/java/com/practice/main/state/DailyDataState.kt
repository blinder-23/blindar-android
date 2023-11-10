package com.practice.main.state

import com.hsk.ktx.date.Date
import com.practice.domain.schedule.Schedule
import com.practice.util.date.DateUtil
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toPersistentList

data class DailyDataState(
    val schoolCode: Int,
    val date: Date,
    val mealUiState: MealUiState,
    val scheduleUiState: ScheduleUiState,
) : Comparable<DailyDataState> {
    override fun compareTo(other: DailyDataState): Int {
        return date.compareTo(other.date)
    }

    companion object {
        private const val EMPTY_SCHOOL_CODE = -1

        val sample = DailyDataState(
            schoolCode = EMPTY_SCHOOL_CODE,
            date = Date(2022, 10, 11),
            mealUiState = MealUiState(
                year = 2022,
                month = 10,
                day = 11,
                menus = (1..6).map { Menu("식단 $it") }.toPersistentList(),
                nutrients = (0..3).map { Nutrient("탄수화물", 123.0, "g") }.toImmutableList()
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
        val Empty = DailyDataState(
            schoolCode = EMPTY_SCHOOL_CODE,
            date = DateUtil.today(),
            mealUiState = MealUiState.EmptyMealState,
            scheduleUiState = ScheduleUiState.EmptyScheduleState,
        )
    }
}