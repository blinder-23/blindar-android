package com.practice.di.meal

import com.practice.neis.meal.retrofit.MealApi
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class MealApiModuleTest {
    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var scheduleApi: MealApi

    @Test
    fun injectScheduleApi() {
        assertFalse(::scheduleApi.isInitialized)
        hiltRule.inject()
        assertTrue(::scheduleApi.isInitialized)
    }
}