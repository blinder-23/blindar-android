package com.practice.di.meal

import com.practice.neis.meal.RemoteMealDataSource
import com.practice.neis.meal.RemoteMealRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
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