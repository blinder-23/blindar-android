package com.practice.di.meal

import com.practice.database.meal.room.MealDao
import com.practice.database.meal.room.MealDatabase
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class MealRoomModuleTest {
    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var mealDatabase: MealDatabase

    @Inject
    lateinit var mealDao: MealDao

    @Test
    fun injectMealDatabase() {
        assertFalse(::mealDatabase.isInitialized)
        hiltRule.inject()
        assertTrue(::mealDatabase.isInitialized)
    }

    @Test
    fun injectMealDao() {
        assertFalse(::mealDao.isInitialized)
        hiltRule.inject()
        assertTrue(::mealDao.isInitialized)
    }
}