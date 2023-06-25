package com.practice.api.school

import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.assertFalse
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class RemoteSchoolModuleTest {
    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var dataSource: RemoteSchoolDataSource

    @Inject
    lateinit var repository: RemoteSchoolRepository

    @Test
    fun testDataSourceIsInjected() {
        assertFalse(::dataSource.isInitialized)
        hiltRule.inject()
        assert(::dataSource.isInitialized)
        assert(dataSource is RemoteSchoolDataSourceImpl)
    }

    @Test
    fun testRepositoryIsInjected() {
        assertFalse(::repository.isInitialized)
        hiltRule.inject()
        assert(::repository.isInitialized)
    }
}