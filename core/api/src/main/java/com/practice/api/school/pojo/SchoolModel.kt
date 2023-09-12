package com.practice.api.school.pojo

import com.google.gson.annotations.SerializedName

data class SchoolModel(
    @SerializedName("school_code") val schoolCode: Int,
    @SerializedName("school_name") val schoolName: String,
    @SerializedName("atpt_ofcdc_sc_code") val officeCode: String,
)
