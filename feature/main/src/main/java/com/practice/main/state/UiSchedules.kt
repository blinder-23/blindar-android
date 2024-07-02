package com.practice.main.state

import com.hsk.ktx.date.Date
import com.practice.domain.schedule.Schedule
import com.practice.main.main.state.MemoDialogElement
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

/**
 * Ui state of schedule
 */

data class UiSchedules(
    val date: Date,
    val uiSchedules: ImmutableList<UiSchedule>,
) {
    val isEmpty: Boolean
        get() = uiSchedules.isEmpty()

    val description: String
        get() = if (uiSchedules.isEmpty()) "학사일정이 없습니다." else uiSchedules.joinToString(", ") { it.displayText }

    companion object {
        val EmptyUiSchedules = UiSchedules(
            date = Date(1900, 1, 1),
            uiSchedules = persistentListOf(),
        )
    }
}

data class UiSchedule(
    val schoolCode: Int,
    val id: Long,
    val year: Int,
    val month: Int,
    val day: Int,
    val eventName: String,
    val eventContent: String
) : MemoDialogElement {
    override val sortOrder: Int
        get() = 1

    override val displayText: String
        get() = if (eventName == eventContent) eventName else "$eventName - $eventContent"
}

fun List<Schedule>.toUiSchedule() = map { it.toUiSchedule() }

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