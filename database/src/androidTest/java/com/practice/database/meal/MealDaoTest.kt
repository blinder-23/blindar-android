package com.practice.database.meal

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.practice.database.TestUtil
import com.practice.database.meal.room.MealDao
import com.practice.database.meal.room.MealDatabase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class MealDaoTest {
    private lateinit var database: MealDatabase
    private lateinit var dao: MealDao

    @Before
    fun createDao() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context,
            MealDatabase::class.java
        ).build()
        dao = database.mealDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDatabase() = runTest {
        database.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeMealAndRead() = runTest {
        val meals = TestUtil.createMealEntityRoom(5)
        dao.insertMeals(meals)
        val mealInDatabase = dao.getMeals(meals[0].date.substring(0..5)).first()
        assertThat(mealInDatabase).isEqualTo(meals)
    }
}