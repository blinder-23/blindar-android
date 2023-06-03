package com.practice.meal.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object MealModule {

    @Provides
    fun provideLocalMealDataSource(
        mealDao: com.practice.meal.room.MealDao
    ): com.practice.meal.LocalMealDataSource = com.practice.meal.LocalMealDataSource(mealDao)

    @Provides
    fun provideMealRepository(
        localMealDataSource: com.practice.meal.LocalMealDataSource
    ): com.practice.meal.MealRepository = com.practice.meal.MealRepository(localMealDataSource)

}