package com.practice.api.schedule.pojo

import com.google.gson.annotations.SerializedName

data class ScheduleModel(
    val id: Int,
    val date: Long,
    @SerializedName("schedule") val title: String,
    val contents: String,
)
