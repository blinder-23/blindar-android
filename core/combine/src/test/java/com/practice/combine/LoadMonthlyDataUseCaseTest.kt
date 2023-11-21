package com.practice.combine

import com.practice.api.memo.FakeRemoteMemoDataSource
import com.practice.api.memo.RemoteMemoRepository
import com.practice.meal.FakeMealDataSource
import com.practice.meal.MealRepository
import com.practice.memo.FakeMemoDataSource
import com.practice.memo.MemoRepository
import com.practice.schedule.FakeScheduleDataSource
import com.practice.schedule.ScheduleRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
class LoadMonthlyDataUseCaseTest {
    private val mealRepository = MealRepository(FakeMealDataSource())
    private val scheduleRepository = ScheduleRepository(FakeScheduleDataSource())
    private val memoRepository = MemoRepository(FakeMemoDataSource())
    private val remoteMemoRepository = RemoteMemoRepository(FakeRemoteMemoDataSource())

    private val useCase = LoadMonthlyDataUseCase(
        localMealRepository = mealRepository,
        localScheduleRepository = scheduleRepository,
        localMemoRepository = memoRepository,
        remoteMemoRepository = remoteMemoRepository,
    )

    @BeforeEach
    fun setUp() = runTest {
        mealRepository.clear()
        scheduleRepository.clear()
        memoRepository.clearMemoDatabase()
    }

    @Test
    fun `check if use case loads all data exactly`() = runTest {
        insertMeals()
        insertSchedules()
        insertMemos()

        val schoolCode = TestUtil.schoolCode
        val userId = TestUtil.userId

        val mealData = useCase.loadMealData(schoolCode, 2022, 8).first()
        val scheduleData = useCase.loadScheduleData(schoolCode, 2022, 8).first()
        val memoData = useCase.loadMemoData(userId, 2022, 8).first()

        val expected = MonthlyData(schoolCode, 2022, 8, mealData, scheduleData, memoData)
        val actual = useCase.loadData(userId, schoolCode, 2022, 8).first()
        assertThat(actual).isEqualTo(expected)
    }

    private suspend fun insertMeals() {
        val meals = (1..10).map { TestUtil.createMealEntity(day = it) }
        mealRepository.insertMeals(meals)
    }

    private suspend fun insertSchedules() {
        val schedules = (1..10).map { TestUtil.createScheduleEntity(day = it) }
        scheduleRepository.insertSchedules(schedules)
    }

    private suspend fun insertMemos() {
        val memos = (1..10).map { TestUtil.createMemo(id = it.toString()) }
        memoRepository.insertMemos(memos)
    }
}