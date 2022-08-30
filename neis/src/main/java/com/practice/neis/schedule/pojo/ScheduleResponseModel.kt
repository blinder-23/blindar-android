package com.practice.neis.schedule.pojo

import com.practice.neis.common.pojo.Header
import com.practice.neis.common.pojo.ResponseModel

data class ScheduleResponseModel(
    override val header: Header,
    override val data: List<ScheduleModel>
) : ResponseModel<ScheduleModel>
