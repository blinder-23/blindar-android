package com.example.server.di.schedule

import com.example.server.schedule.RemoteScheduleDataSource
import com.example.server.schedule.RemoteScheduleRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.assertFalse
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class RemoteScheduleModuleTest {
    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var dataSource: RemoteScheduleDataSource

    @Inject
    lateinit var repository: RemoteScheduleRepository

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