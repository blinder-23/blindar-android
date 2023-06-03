package com.practice.api.meal

import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.assertFalse
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class RemoteMealModuleTest {
    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var dataSource: com.practice.api.meal.RemoteMealDataSource

    @Inject
    lateinit var repository: com.practice.api.meal.RemoteMealRepository

    @Test
    fun testDataSourceIsInjected() {
        assertFalse(::dataSource.isInitialized)
        hiltRule.inject()
        assert(::dataSource.isInitialized)
    }

    @Test
    fun testRepositoryIsInjected() {
        assertFalse(::repository.isInitialized)
        hiltRule.inject()
        assert(::repository.isInitialized)
    }
}