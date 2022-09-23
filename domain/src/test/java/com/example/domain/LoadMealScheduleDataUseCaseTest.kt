package com.example.domain

import com.example.domain.combine.LoadMealScheduleDataUseCase
import com.example.domain.combine.MealScheduleEntity
import com.example.domain.combine.toMealEntity
import com.practice.database.meal.FakeMealDataSource
import com.practice.database.meal.MealRepository
import com.practice.database.schedule.FakeScheduleDataSource
import com.practice.database.schedule.ScheduleRepository
import com.practice.neis.meal.FakeRemoteMealDataSource
import com.practice.neis.meal.RemoteMealRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
class LoadMealScheduleDataUseCaseTest {
    private val localMealRepository = MealRepository(FakeMealDataSource())
    private val localScheduleRepository = ScheduleRepository(FakeScheduleDataSource())
    private val remoteMealRepository = RemoteMealRepository(FakeRemoteMealDataSource())
    private val useCase = LoadMealScheduleDataUseCase(
        localMealRepository = localMealRepository,
        localScheduleRepository = localScheduleRepository,
        remoteMealRepository = remoteMealRepository,
    )

    @BeforeEach
    fun setUp() = runTest {
        localMealRepository.clear()
        localScheduleRepository.clear()
    }

    @Test
    fun `check if use case detects empty meal data`() = runTest {
        val exists = useCase.checkMealExists(2022, 8)
        assertThat(exists).isFalse
    }

    @Test
    fun `check if use case detects meal data`() = runTest {
        val meals = (1..10).map { TestUtil.createMealEntity(day = it) }
        localMealRepository.insertMeals(meals)

        val meal = meals.first()
        val exists = useCase.checkMealExists(meal.year, meal.month)
        assertThat(exists).isTrue
    }

    @Test
    fun `check if use case detects empty schedule data`() = runTest {
        val exists = useCase.checkScheduleExists(2022, 8)
        assertThat(exists).isFalse
    }

    @Test
    fun `check if use case detects schedule data`() = runTest {
        val schedules = (1..10).map { TestUtil.createScheduleEntity(day = it) }
        localScheduleRepository.insertSchedules(schedules)

        val schedule = schedules.first()
        val exists = useCase.checkScheduleExists(schedule.year, schedule.month)
        assertThat(exists).isTrue
    }

    @Test
    fun `check if use case detects complicated schedule data`() = runTest {
        val schedules = (1..10).map { TestUtil.createScheduleEntity(year = 2020 + it, month = it) }
        localScheduleRepository.insertSchedules(schedules)

        val schedule = schedules.first()
        val exists = useCase.checkScheduleExists(schedule.year, schedule.month)
        assertThat(exists).isTrue
    }

    @Test
    fun `check if use case loads meal data from remote`() = runTest {
        val exists = useCase.checkMealExists(2022, 8)
        assertThat(exists).isFalse

        val remoteRealData = remoteMealRepository.getMealData(2022, 8)
        val remoteUseCaseData = useCase.loadMealFromRemote(2022, 8)
        assertThat(remoteUseCaseData).containsExactlyInAnyOrderElementsOf(remoteRealData)
    }

    @Test
    fun `check if use case stores meal data to local`() = runTest {
        val meals = remoteMealRepository.getMealData(2022, 8)
        useCase.storeMealToLocal(meals)

        val mealEntities = meals.map { it.toMealEntity() }
        val storedMeals = localMealRepository.getMeals(2022, 8)
        assertThat(storedMeals).containsExactlyInAnyOrderElementsOf(mealEntities)
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
    fun `check if use case loads meal data when data doesn't exist at local`() = runTest {
        val expected = remoteMealRepository.getMealData(2022, 8).map { it.toMealEntity() }
        val dataFromLocal = useCase.loadMealData(2022, 8)
        assertThat(dataFromLocal).containsExactlyInAnyOrderElementsOf(expected)
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