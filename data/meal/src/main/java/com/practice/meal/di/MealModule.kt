package com.practice.meal.di

import com.practice.meal.LocalMealDataSource
import com.practice.meal.MealRepository
import com.practice.meal.room.MealDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object MealModule {

    @Provides
    fun provideLocalMealDataSource(
        mealDao: MealDao
    ): LocalMealDataSource = LocalMealDataSource(mealDao)

    @Provides
    fun provideMealRepository(
        localMealDataSource: LocalMealDataSource
    ): MealRepository = MealRepository(localMealDataSource)

}