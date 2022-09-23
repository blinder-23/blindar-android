package com.example.di

import com.example.domain.combine.LoadMealScheduleDataUseCase
import com.practice.database.meal.MealRepository
import com.practice.database.schedule.ScheduleRepository
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
    ) = LoadMealScheduleDataUseCase(
        localMealRepository,
        localScheduleRepository,
    )

}