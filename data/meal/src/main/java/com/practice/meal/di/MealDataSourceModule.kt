package com.practice.meal.di

import com.practice.meal.LocalMealDataSource
import com.practice.meal.MealDataSource
import com.practice.meal.room.MealDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object MealDataSourceModule {

    @Provides
    fun provideLocalMealDataSource(
        mealDao: MealDao
    ): MealDataSource = LocalMealDataSource(mealDao)

}