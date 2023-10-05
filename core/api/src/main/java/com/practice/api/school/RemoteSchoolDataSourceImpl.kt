package com.practice.api.school

import android.util.Log
import com.practice.api.school.api.SchoolApi
import com.practice.api.school.pojo.SchoolModel
import javax.inject.Inject

class RemoteSchoolDataSourceImpl @Inject constructor(private val api: SchoolApi) :
    RemoteSchoolDataSource {
    override suspend fun getSupportedSchools(): List<SchoolModel> {
        return try {
            api.getSupportedSchools().data.apply {
                Log.d(TAG, "schools: ${this.map { it.schoolName }}")
            }.sortedBy { it.schoolName }
        } catch (e: Exception) {
            Log.e(TAG, "School api error", e)
            emptyList()
        }
    }

    companion object {
        private const val TAG = "RemoteSchoolDataSourceImpl"
    }
}