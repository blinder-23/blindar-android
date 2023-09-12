package com.practice.api.school

import com.practice.api.school.pojo.SchoolModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RemoteSchoolRepository(
    private val dataSource: RemoteSchoolDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    private lateinit var supportedSchools: List<SchoolModel>

    suspend fun getSupportedSchools(): List<SchoolModel> {
        if (!::supportedSchools.isInitialized) {
            withContext(ioDispatcher) {
                supportedSchools = dataSource.getSupportedSchools()
            }
        }
        return supportedSchools
    }

    suspend fun searchSupportedSchools(query: String): List<SchoolModel> {
        return withContext(ioDispatcher) {
            getSupportedSchools().filter { it.schoolName.contains(query) }
        }
    }
}