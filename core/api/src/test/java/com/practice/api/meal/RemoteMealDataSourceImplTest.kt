package com.practice.api.meal

import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class RemoteMealDataSourceImplTest {
    private val remote: com.practice.api.meal.RemoteMealDataSource =
        com.practice.api.meal.RemoteMealDataSourceImpl(com.practice.api.meal.api.mealApi)

    @Test
    fun testRemote(): Unit = runBlocking {
        val year = 2022
        val month = 10
        val result = remote.getMeals(year, month)

        assertThat(result.response)
            .isNotEmpty
            .allSatisfy {
                val responseYear = it.ymd.substring(0..3).toInt()
                val responseMonth = it.ymd.substring(4..5).toInt()
                assertThat(responseYear).isEqualTo(year)
                assertThat(responseMonth).isEqualTo(month)
            }
    }
}