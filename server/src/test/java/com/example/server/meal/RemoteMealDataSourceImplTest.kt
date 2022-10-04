package com.example.server.meal

import com.example.server.meal.api.mealApi
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class RemoteMealDataSourceImplTest {
    private val remote: RemoteMealDataSource = RemoteMealDataSourceImpl(mealApi)

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