package com.practice.di.meal

import com.practice.database.meal.LocalMealDataSource
import com.practice.database.meal.MealRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class MealModuleTest {
    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var localMealDataSource: LocalMealDataSource

    @Inject
    lateinit var mealRepository: MealRepository

    @Test
    fun injectLocalMealDataSource() {
        assertFalse(::localMealDataSource.isInitialized)
        hiltRule.inject()
        assertTrue(::localMealDataSource.isInitialized)
    }

    @Test
    fun injectMealRepository() {
        assertFalse(::mealRepository.isInitialized)
        hiltRule.inject()
        assertTrue(::mealRepository.isInitialized)
    }
}