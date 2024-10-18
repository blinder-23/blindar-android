package com.practice.api.user.pojo

import com.google.gson.annotations.SerializedName
import com.practice.api.school.pojo.SchoolModel

data class UserSchoolResponse(
    @SerializedName("is_success") val isSuccess: Boolean,
    @SerializedName("response") val school: SchoolModel,
)