package com.example.domain.di

import com.example.domain.combine.LoadMealScheduleDataUseCase
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class UseCaseModuleTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var useCase: LoadMealScheduleDataUseCase

    @Test
    fun injectUseCase() {
        assertThat(::useCase.isInitialized).isFalse
        hiltRule.inject()
        assertThat(::useCase.isInitialized).isTrue
    }

}