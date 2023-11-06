package com.practice.combine.di

import com.practice.combine.LoadMonthlyDataUseCase
import com.practice.meal.MealRepository
import com.practice.schedule.ScheduleRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun provideLoadMealScheduleDataUseCase(
        localMealRepository: MealRepository,
        localScheduleRepository: ScheduleRepository,
    ) = LoadMonthlyDataUseCase(
        localMealRepository,
        localScheduleRepository,
    )

}