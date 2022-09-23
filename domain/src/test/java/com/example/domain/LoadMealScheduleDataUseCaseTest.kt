package com.example.domain

import com.example.domain.combine.LoadMealScheduleDataUseCase
import com.example.domain.combine.MealScheduleEntity
import com.practice.database.meal.FakeMealDataSource
import com.practice.database.meal.MealRepository
import com.practice.database.schedule.FakeScheduleDataSource
import com.practice.database.schedule.ScheduleRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
    fun `check if use case loads meal data from local`() = runTest {
        val meals = (1..20).map { TestUtil.createMealEntity(day = it) }
        localMealRepository.insertMeals(meals)

        val localData = useCase.loadMealFromLocal(meals[0].year, meals[0].month)
        assertThat(localData).containsExactlyInAnyOrderElementsOf(meals)
    }

    @Test
    fun `check if use case loads schedule data from local`() = runTest {
        val schedules = (1..15).map { TestUtil.createScheduleEntity(day = it) }
        localScheduleRepository.insertSchedules(schedules)

        val localData = useCase.loadScheduleFromLocal(schedules[0].year, schedules[0].month)
        assertThat(localData).containsExactlyInAnyOrderElementsOf(schedules)
    }

    @Test
    fun `check if use case loads all data exactly`() = runTest {
        val mealData = useCase.loadMealData(2022, 8)
        val scheduleData = useCase.loadScheduleData(2022, 8)
        val expected = MealScheduleEntity(2022, 8, mealData, scheduleData)
        val actual = useCase.loadData(2022, 8)
        assertThat(actual).isEqualTo(expected)
    }
}