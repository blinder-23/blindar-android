package com.example.di.meal

import com.example.server.meal.RemoteMealDataSource
import com.example.server.meal.RemoteMealRepository
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
    lateinit var dataSource: RemoteMealDataSource

    @Inject
    lateinit var repository: RemoteMealRepository

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