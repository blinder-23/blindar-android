package com.example.di

import com.example.domain.combine.LoadMealScheduleDataUseCase
import com.example.domain.date.CalculateDayTypeUseCase
import com.practice.database.meal.MealRepository
import com.practice.database.schedule.ScheduleRepository
import com.practice.neis.meal.MealRemoteRepository
import com.practice.neis.schedule.ScheduleRemoteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun provideLoadMealScheduleDataUseCase(
        mealLocalRepository: MealRepository,
        scheduleLocalRepository: ScheduleRepository,
        mealRemoteRepository: MealRemoteRepository,
        scheduleRemoteRepository: ScheduleRemoteRepository,
    ) = LoadMealScheduleDataUseCase(
        mealLocalRepository,
        scheduleLocalRepository,
        mealRemoteRepository,
        scheduleRemoteRepository
    )

    @Provides
    fun provideCalculateDayTypeUseCase() = CalculateDayTypeUseCase()

}