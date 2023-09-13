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
class LocalMealDataSourceTest {
    private lateinit var database: MealDatabase
    private lateinit var source: LocalMealDataSource

    @Before
    fun init() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context,
            MealDatabase::class.java
        ).build()
        source = LocalMealDataSource(database.mealDao())
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun source_insertMeals() = runTest {
        val meals = TestUtil.createMealEntity(10)
        source.insertMeals(meals)

        val mealsInDatabase =
            source.getMeals(TestUtil.schoolCode, meals[0].year, meals[0].month).first()
        assertThat(mealsInDatabase).containsExactlyInAnyOrderElementsOf(meals)
    }

    @Test
    fun source_deleteMeals() = runTest {
        val meals = TestUtil.createMealEntity(10)
        source.insertMeals(meals)
        source.deleteMeals(meals)

        val mealsInDatabase =
            source.getMeals(TestUtil.schoolCode, meals[0].year, meals[0].month).first()
        assertThat(mealsInDatabase).isEmpty()
    }

    @Test
    fun source_deleteMeals_ByDate() = runTest {
        val meals = TestUtil.createMealEntity(10)
        source.insertMeals(meals)
        source.deleteMeals(TestUtil.schoolCode, meals[0].year, meals[0].month)

        val mealsInDatabase =
            source.getMeals(TestUtil.schoolCode, meals[0].year, meals[0].month).first()
        assertThat(mealsInDatabase).isEmpty()
    }

    @Test
    fun source_clear() = runTest {
        val meals = TestUtil.createMealEntity(10)
        source.insertMeals(meals)
        source.clear()

        val mealsInDatabase =
            source.getMeals(TestUtil.schoolCode, meals[0].year, meals[0].month).first()
        assertThat(mealsInDatabase).isEmpty()
    }

}