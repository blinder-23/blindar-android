package com.practice.di

import com.practice.neis.meal.MealRemoteDataSource
import com.practice.neis.meal.MealRemoteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent


@Module
@InstallIn(ViewModelComponent::class)
abstract class MealRemoteModule {

    @Provides
    fun provideMealRemoteRepository(
        mealRemoteDataSource: MealRemoteDataSource
    ): MealRemoteRepository = MealRemoteRepository(mealRemoteDataSource)
}