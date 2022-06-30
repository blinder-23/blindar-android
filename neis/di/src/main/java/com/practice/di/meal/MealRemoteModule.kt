package com.practice.di.meal

import com.practice.neis.meal.MealRemoteDataSource
import com.practice.neis.meal.MealRemoteRepository
import com.practice.neis.meal.retrofit.MealApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object MealRemoteModule {

    @Provides
    fun provideMealRemoteDataSource(
        api: MealApi
    ): MealRemoteDataSource = MealRemoteDataSource(api)

    @Provides
    fun provideMealRemoteRepository(
        mealRemoteDataSource: MealRemoteDataSource
    ): MealRemoteRepository = MealRemoteRepository(mealRemoteDataSource)
}