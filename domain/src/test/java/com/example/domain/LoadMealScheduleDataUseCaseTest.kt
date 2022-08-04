package com.example.domain

import com.practice.database.meal.MealFakeDataSource
import com.practice.database.meal.MealRepository
import com.practice.database.schedule.ScheduleFakeDataSource
import com.practice.database.schedule.ScheduleRepository
import com.practice.neis.meal.MealFakeRemoteDataSource
import com.practice.neis.meal.MealRemoteRepository
import com.practice.neis.schedule.ScheduleFakeRemoteDataSource
import com.practice.neis.schedule.ScheduleRemoteRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
class LoadMealScheduleDataUseCaseTest {
    private val mealLocalRepository = MealRepository(MealFakeDataSource())
    private val scheduleLocalRepository = ScheduleRepository(ScheduleFakeDataSource())
    private val mealRemoteRepository = MealRemoteRepository(MealFakeRemoteDataSource())
    private val scheduleRemoteRepository = ScheduleRemoteRepository(ScheduleFakeRemoteDataSource())
    private val useCase = LoadMealScheduleDataUseCase(
        mealLocalRepository = mealLocalRepository,
        scheduleLocalRepository = scheduleLocalRepository,
        mealRemoteRepository = mealRemoteRepository,
        scheduleRemoteRepository = scheduleRemoteRepository
    )

    @BeforeEach
    fun setUp() = runTest {
        mealLocalRepository.clear()
        scheduleLocalRepository.clear()
    }

    @Test
    fun `check if use case detects empty meal data`() = runTest {
        val exists = useCase.checkMealExists(2022, 8)
        assertThat(exists).isFalse
    }

    @Test
    fun `check if use case detects meal data`() = runTest {
        val meals = (1..10).map { TestUtil.createMealEntity(day = it) }
        mealLocalRepository.insertMeals(meals)

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
        scheduleLocalRepository.insertSchedules(schedules)

        val schedule = schedules.first()
        val exists = useCase.checkScheduleExists(schedule.year, schedule.month)
        assertThat(exists).isTrue
    }

    @Test
    fun `check if use case detects complicated schedule data`() = runTest {
        val schedules = (1..10).map { TestUtil.createScheduleEntity(year = 2020 + it, month = it) }
        scheduleLocalRepository.insertSchedules(schedules)

        val schedule = schedules.first()
        val exists = useCase.checkScheduleExists(schedule.year, schedule.month)
        assertThat(exists).isTrue
    }

    @Test
    fun `check if use case loads meal data from remote`() = runTest {
        val exists = useCase.checkMealExists(2022, 8)
        assertThat(exists).isFalse

        val remoteRealData = mealRemoteRepository.getMealData(2022, 8)
        val remoteUseCaseData = useCase.loadMealFromRemote(2022, 8)
        assertThat(remoteUseCaseData).containsExactlyInAnyOrderElementsOf(remoteRealData)
    }

    @Test
    fun `check if use case stores meal data to local`() = runTest {
        val meals = mealRemoteRepository.getMealData(2022, 8)
        useCase.storeMealToLocal(meals)

        val mealEntities = meals.map { it.toMealEntity() }
        val storedMeals = mealLocalRepository.getMeals(2022, 8)
        assertThat(storedMeals).containsExactlyInAnyOrderElementsOf(mealEntities)
    }

    @Test
    fun `check if use case loads meal data from local`() = runTest {
        val meals = (1..20).map { TestUtil.createMealEntity(day = it) }
        mealLocalRepository.insertMeals(meals)

        val localData = useCase.loadMealFromLocal(meals[0].year, meals[0].month)
        assertThat(localData).containsExactlyInAnyOrderElementsOf(meals)
    }

    @Test
    fun `check if use case loads schedule data from remote`() = runTest {
        val exists = useCase.checkScheduleExists(2022, 8)
        assertThat(exists).isFalse

        val remoteRealData = scheduleRemoteRepository.getSchedules(2022, 8)
        val remoteUseCaseData = useCase.loadScheduleFromRemote(2022, 8)
        assertThat(remoteUseCaseData).containsExactlyInAnyOrderElementsOf(remoteRealData)
    }

    @Test
    fun `check if use case stores schedule data to local`() = runTest {
        val schedules = scheduleRemoteRepository.getSchedules(2022, 8)
        useCase.storeScheduleToLocal(schedules)

        val scheduleEntities = schedules.map { it.toScheduleEntity() }
        val storedSchedules = scheduleLocalRepository.getSchedules(2022, 8)
        assertThat(storedSchedules).containsExactlyInAnyOrderElementsOf(scheduleEntities)
    }

    @Test
    fun `check if use case loads schedule data from local`() = runTest {
        val schedules = (1..15).map { TestUtil.createScheduleEntity(day = it) }
        scheduleLocalRepository.insertSchedules(schedules)

        val localData = useCase.loadScheduleFromLocal(schedules[0].year, schedules[0].month)
        assertThat(localData).containsExactlyInAnyOrderElementsOf(schedules)
    }
}