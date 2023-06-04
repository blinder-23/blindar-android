package com.practice.meal.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object MealDataSourceModule {

    @Provides
    fun provideLocalMealDataSource(
        mealDao: com.practice.meal.room.MealDao
    ): com.practice.meal.MealDataSource = com.practice.meal.LocalMealDataSource(mealDao)

}