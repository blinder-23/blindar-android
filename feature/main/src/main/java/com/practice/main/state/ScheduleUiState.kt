package com.practice.main.state

import com.practice.domain.schedule.Schedule
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

/**
 * Ui state of schedule
 */

data class ScheduleUiState(
    val uiSchedules: ImmutableList<UiSchedule>,
) {
    val isEmpty: Boolean
        get() = uiSchedules.isEmpty()

    val description: String
        get() = if (uiSchedules.isEmpty()) "학사일정이 없습니다." else uiSchedules.joinToString(", ") { it.displayText }

    companion object {
        val EmptyScheduleState = ScheduleUiState(persistentListOf())
    }
}

data class UiSchedule(
    val schoolCode: Int,
    val id: Int,
    val year: Int,
    val month: Int,
    val day: Int,
    val eventName: String,
    val eventContent: String
) : MemoPopupElement {
    val displayText: String
        get() = if (eventName == eventContent) eventName else "$eventName - $eventContent"
}

fun Schedule.toUiSchedule() = UiSchedule(
    schoolCode = schoolCode,
    id = id,
    year = year,
    month = month,
    day = day,
    eventName = eventName,
    eventContent = eventContent,
)

fun UiSchedule.toSchedule() = Schedule(
    schoolCode = schoolCode,
    id = id,
    year = year,
    month = month,
    day = day,
    eventName = eventName,
    eventContent = eventContent,
)