package com.practice.hanbitlunch.screen.core

import com.hsk.ktx.date.Date
import com.practice.database.meal.entity.MealEntity
import com.practice.database.schedule.entity.ScheduleEntity
import com.practice.hanbitlunch.calendar.core.YearMonth
import com.practice.preferences.ScreenMode
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList

data class MainUiState(
    val year: Int,
    val month: Int,
    val selectedDate: Date,
    val monthlyMealScheduleState: List<DailyMealScheduleState>,
    val isLoading: Boolean,
    val screenMode: ScreenMode,
) {
    val yearMonth: YearMonth
        get() = YearMonth(year, month)
}

/**
 * Ui state of meal
 */

data class MealUiState(
    val menus: ImmutableList<Menu>
) {
    val isEmpty: Boolean
        get() = menus.isEmpty()

    val description: String
        get() = if (menus.isEmpty()) "식단이 없습니다." else menus.joinToString(", ") { it.name }

    companion object {
        val EmptyMealState = MealUiState(persistentListOf())
    }
}

fun MealEntity.toMealUiState() = if (this.menus.isEmpty()) {
    MealUiState.EmptyMealState
} else {
    MealUiState(menus.filter { it.name != "급식우유" }.map { Menu(it.name) }.toPersistentList())
}

data class Menu(
    val name: String,
)

/**
 * Ui state of schedule
 */

data class ScheduleUiState(
    val schedules: ImmutableList<Schedule>,
) {
    val isEmpty: Boolean
        get() = schedules.isEmpty()

    val description: String
        get() = if (schedules.isEmpty()) "학사일정이 없습니다." else schedules.joinToString(", ") { it.displayText }

    companion object {
        val EmptyScheduleState = ScheduleUiState(persistentListOf())
    }
}

data class Schedule(
    private val scheduleName: String,
    private val scheduleContent: String,
) {
    val displayText =
        if (scheduleName == scheduleContent) scheduleName else "$scheduleName - $scheduleContent"
}

fun ScheduleEntity.toSchedule() = Schedule(
    scheduleName = eventName,
    scheduleContent = eventContent,
)