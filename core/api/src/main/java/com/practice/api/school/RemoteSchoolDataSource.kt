package com.practice.api.school

import com.practice.api.school.pojo.SchoolModel

interface RemoteSchoolDataSource {
    suspend fun getSupportedSchools(): List<SchoolModel>
}