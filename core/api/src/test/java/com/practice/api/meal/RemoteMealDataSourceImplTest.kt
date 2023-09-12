package com.practice.api.meal

import com.practice.api.meal.api.mealApi

class RemoteMealDataSourceImplTest {
    private val remote: RemoteMealDataSource = RemoteMealDataSourceImpl(mealApi)

    // TODO: 학사일정 api 고쳐지면 다시 켜기
//    @Test
//    fun testRemote(): Unit = runBlocking {
//        val schoolCode = 7010578
//        val year = 2022
//        val month = 10
//        val result = remote.getMeals(schoolCode, year, month)
//
//        assertThat(result.response)
//            .isNotEmpty
//            .allSatisfy {
//                val responseYear = it.ymd.substring(0..3).toInt()
//                val responseMonth = it.ymd.substring(4..5).toInt()
//                assertThat(responseYear).isEqualTo(year)
//                assertThat(responseMonth).isEqualTo(month)
//            }
//    }
}