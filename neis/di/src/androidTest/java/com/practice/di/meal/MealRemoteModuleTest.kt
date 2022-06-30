package com.practice.di.meal

import com.practice.neis.meal.MealRemoteDataSource
import com.practice.neis.meal.MealRemoteRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class MealRemoteModuleTest {
    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var dataSource: MealRemoteDataSource

    @Inject
    lateinit var repository: MealRemoteRepository

    @Test
    fun injectDataSource() {
        assertFalse(::dataSource.isInitialized)
        hiltRule.inject()
        assertTrue(::dataSource.isInitialized)
    }

    @Test
    fun injectRepository() {
        assertFalse(::repository.isInitialized)
        hiltRule.inject()
        assertTrue(::repository.isInitialized)
    }
}