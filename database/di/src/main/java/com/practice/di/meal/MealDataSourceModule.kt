package com.practice.di.meal

import com.practice.database.meal.MealDataSource
import com.practice.database.meal.LocalMealDataSource
import com.practice.database.meal.room.MealDao
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