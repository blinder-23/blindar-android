package com.practice.di.meal

import com.practice.neis.meal.MealRemoteDataSource
import com.practice.neis.meal.MealRemoteDataSourceImpl
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
    fun provideMealRemoteDataSourceImpl(
        api: MealApi
    ): MealRemoteDataSource = MealRemoteDataSourceImpl(api)

    @Provides
    fun provideMealRemoteRepository(
        mealRemoteDataSource: MealRemoteDataSource
    ): MealRemoteRepository = MealRemoteRepository(mealRemoteDataSource)
}