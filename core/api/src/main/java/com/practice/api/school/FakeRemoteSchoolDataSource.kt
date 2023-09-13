package com.practice.api.school

import com.practice.api.school.pojo.SchoolModel
import kotlinx.coroutines.delay
import javax.inject.Inject

class FakeRemoteSchoolDataSource @Inject constructor() : RemoteSchoolDataSource {
    override suspend fun getSupportedSchools(): List<SchoolModel> {
        delay(500L)
        return (1..15).map {
            SchoolModel(
                schoolName = "학교 $it",
                schoolCode = it,
                officeCode = it.toString(),
            )
        }
    }
}