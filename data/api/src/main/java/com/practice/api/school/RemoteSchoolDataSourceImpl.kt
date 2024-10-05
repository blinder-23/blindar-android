package com.practice.api.school

import com.practice.api.school.api.SchoolApi
import com.practice.api.school.pojo.SchoolModel
import javax.inject.Inject

class RemoteSchoolDataSourceImpl @Inject constructor(private val api: SchoolApi) :
    RemoteSchoolDataSource {
    override suspend fun getSupportedSchools(): List<SchoolModel> {
        return try {
            api.getSupportedSchools().response.sortedBy { it.schoolName }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    companion object {
        private const val TAG = "RemoteSchoolDataSourceImpl"
    }
}