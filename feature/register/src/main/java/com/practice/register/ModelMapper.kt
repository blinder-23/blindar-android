package com.practice.register

import com.practice.api.school.pojo.SchoolModel
import com.practice.register.selectschool.School

fun SchoolModel.toSchool() = School(
    name = name,
    schoolId = schoolId,
)