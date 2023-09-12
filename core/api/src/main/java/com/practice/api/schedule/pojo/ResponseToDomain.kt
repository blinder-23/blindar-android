package com.practice.api.schedule.pojo

import com.practice.api.toEpochDate
import com.practice.domain.schedule.MonthlySchedule
import com.practice.domain.schedule.Schedule


fun ScheduleResponse.toMonthlySchedule(
    schoolCode: Int,
    year: Int,
    month: Int,
) = MonthlySchedule(
    schoolCode = schoolCode,
    year = year,
    month = month,
    schedules = schedules.toScheduleList(),
)

fun List<ScheduleModel>.toScheduleList() = map { it.toSchedule() }

fun ScheduleModel.toSchedule(): Schedule {
    val dateObject = date.toEpochDate(9)
    return Schedule(
        schoolCode = schoolCode,
        year = dateObject.year,
        month = dateObject.month,
        day = dateObject.dayOfMonth,
        id = id,
        eventName = title,
        eventContent = contents,
    )
}