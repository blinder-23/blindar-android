package com.practice.api

import com.practice.api.school.pojo.SchoolModel
import com.practice.domain.School

fun SchoolModel.toSchool() = School(
    name = name,
    schoolId = schoolId,
)