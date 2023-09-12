package com.practice.combine

import com.practice.meal.FakeMealDataSource
import com.practice.meal.MealRepository
import com.practice.schedule.FakeScheduleDataSource
import com.practice.schedule.ScheduleRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
class LoadMealScheduleDataUseCaseTest {
    private val localMealRepository = MealRepository(FakeMealDataSource())
    private val localScheduleRepository = ScheduleRepository(FakeScheduleDataSource())
    private val useCase = LoadMealScheduleDataUseCase(
        localMealRepository = localMealRepository,
        localScheduleRepository = localScheduleRepository,
    )

    @BeforeEach
    fun setUp() = runTest {
        localMealRepository.clear()
        localScheduleRepository.clear()
    }

    @Test
    fun `check if use case loads all data exactly`() = runTest {
        insertMeals()
        insertSchedules()

        val schoolCode = TestUtil.schoolCode
        val mealData = useCase.loadMealData(schoolCode, 2022, 8).first()
        val scheduleData = useCase.loadScheduleData(schoolCode, 2022, 8).first()
        val expected = MealScheduleEntity(schoolCode, 2022, 8, mealData, scheduleData)
        val actual = useCase.loadData(schoolCode, 2022, 8).first()
        assertThat(actual).isEqualTo(expected)
    }

    private suspend fun insertMeals() {
        val meals = (1..10).map { TestUtil.createMealEntity(day = it) }
        localMealRepository.insertMeals(meals)
    }

    private suspend fun insertSchedules() {
        val schedules = (1..10).map { TestUtil.createScheduleEntity(day = it) }
        localScheduleRepository.insertSchedules(schedules)
    }
}