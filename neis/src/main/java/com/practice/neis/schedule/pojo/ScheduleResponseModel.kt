package com.practice.neis.schedule.pojo

import com.practice.neis.common.pojo.Header

data class ScheduleResponseModel(
    val header: Header,
    val scheduleData: List<ScheduleModel>
)
