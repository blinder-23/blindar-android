package com.practice.di.schedule

import com.practice.neis.schedule.retrofit.ScheduleInfo
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class ScheduleApiModuleTest {
    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var api: ScheduleInfo

    @Test
    fun injectApi() {
        assertFalse(::api.isInitialized)
        hiltRule.inject()
        assertTrue(::api.isInitialized)
    }
}