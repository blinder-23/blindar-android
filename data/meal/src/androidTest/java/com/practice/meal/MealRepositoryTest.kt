package com.practice.meal

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.practice.meal.room.MealDatabase
import com.practice.meal.util.TestUtil
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class MealRepositoryTest {
    private lateinit var database: MealDatabase
    private lateinit var source: LocalMealDataSource
    private lateinit var repository: MealRepository

    private val schoolCode = TestUtil.schoolCode

    @Before
    fun init() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context,
            MealDatabase::class.java
        ).build()
        source = LocalMealDataSource(database.mealDao())
        repository = MealRepository(source)
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun repository_getMeals() = runTest {
        val meals = TestUtil.createMealEntity(10)
        repository.insertMeals(meals)

        val actual = repository.getMeals(schoolCode, meals[0].year, meals[0].month).first()
        assertThat(actual).containsExactlyInAnyOrderElementsOf(meals)
    }

    @Test
    fun repository_insertMeals_vararg() = runTest {
        val (meal1, meal2, meal3) = TestUtil.createMealEntity(3)
        repository.insertMeals(meal1, meal2, meal3)

        val actual = repository.getMeals(schoolCode, meal1.year, meal1.month).first()
        assertThat(actual).containsExactlyInAnyOrderElementsOf(listOf(meal1, meal2, meal3))
    }

    @Test
    fun repository_deleteMeals_vararg() = runTest {
        val (meal1, meal2, meal3) = TestUtil.createMealEntity(3)
        repository.insertMeals(meal1, meal2, meal3)
        repository.deleteMeals(meal1, meal2, meal3)

        val actual = repository.getMeals(schoolCode, meal1.year, meal1.month).first()
        assertThat(actual).isEmpty()
    }

    @Test
    fun repository_deleteMeals_ByDate() = runTest {
        val meals = TestUtil.createMealEntity(10)
        repository.insertMeals(meals)
        repository.deleteMeals(schoolCode, meals[0].year, meals[0].month)

        val actual = repository.getMeals(schoolCode, meals[0].year, meals[0].month).first()
        assertThat(actual).isEmpty()
    }

    @Test
    fun repository_clear() = runTest {
        val meals = TestUtil.createMealEntity(10)
        repository.insertMeals(meals)
        repository.clear()

        val actual = repository.getMeals(schoolCode, meals[0].year, meals[0].month).first()
        assertThat(actual).isEmpty()
    }
}