package com.practice.di.meal

import com.practice.database.meal.MealLocalDataSource
import com.practice.database.meal.MealRepository
import com.practice.database.meal.room.MealDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object MealModule {

    @Provides
    fun provideMealLocalDataSource(
        mealDao: MealDao
    ): MealLocalDataSource = MealLocalDataSource(mealDao)

    @Provides
    fun provideMealRepository(
        mealLocalDataSource: MealLocalDataSource
    ): MealRepository = MealRepository(mealLocalDataSource)

}