package com.practice.api

import com.practice.api.school.pojo.SchoolModel
import com.practice.domain.School

// TODO: 교육청 코드는 나중에 필요해지면 사용하기
fun SchoolModel.toSchool() = School(
    name = schoolName,
    schoolCode = schoolCode,
)