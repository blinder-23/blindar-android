package com.practice.di.meal

import com.practice.database.meal.LocalMealDataSource
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
    fun provideLocalMealDataSource(
        mealDao: MealDao
    ): LocalMealDataSource = LocalMealDataSource(mealDao)

    @Provides
    fun provideMealRepository(
        localMealDataSource: LocalMealDataSource
    ): MealRepository = MealRepository(localMealDataSource)

}