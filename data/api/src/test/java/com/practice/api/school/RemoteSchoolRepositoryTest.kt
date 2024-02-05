package com.practice.api.school

import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class RemoteSchoolRepositoryTest {
    private val fakeRemoteSchoolDataSource = FakeRemoteSchoolDataSource()
    private val remoteSchoolRepository = RemoteSchoolRepository(fakeRemoteSchoolDataSource)

    @Test
    fun getAll() = runTest {
        val expected = fakeRemoteSchoolDataSource.getSupportedSchools()
        val actual = remoteSchoolRepository.getSupportedSchools()
        assertThat(expected)
            .containsExactlyInAnyOrderElementsOf(actual)
    }

    @Test
    fun emptyQuery() = runTest {
        val expected = fakeRemoteSchoolDataSource.getSupportedSchools()
        val actual = remoteSchoolRepository.searchSupportedSchools("")
        assertThat(expected)
            .containsExactlyInAnyOrderElementsOf(actual)
    }

    @Test
    fun query() = runTest {
        val query = "2"
        val expected =
            fakeRemoteSchoolDataSource.getSupportedSchools().filter { it.schoolName.contains(query) }
        val actual = remoteSchoolRepository.searchSupportedSchools(query)
        assertThat(expected)
            .containsExactlyInAnyOrderElementsOf(actual)
    }
}