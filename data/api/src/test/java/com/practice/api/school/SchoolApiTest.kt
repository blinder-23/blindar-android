package com.practice.api.school

import com.practice.api.school.api.schoolApi
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SchoolApiTest {
    private val api = schoolApi

    @Test
    fun testSchoolApi() = runTest {
        val response = api.getSupportedSchools()
        println(response)
        assertThat(response.responseCode).isEqualTo(200)
        assertThat(response.data)
            .isNotEmpty
    }
}