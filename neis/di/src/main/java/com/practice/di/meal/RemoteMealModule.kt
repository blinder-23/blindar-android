package com.practice.di.meal

import com.practice.neis.meal.RemoteMealDataSource
import com.practice.neis.meal.RemoteMealDataSourceImpl
import com.practice.neis.meal.RemoteMealRepository
import com.practice.neis.meal.retrofit.MealApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object RemoteMealModule {

    @Provides
    fun provideRemoteMealDataSourceImpl(
        api: MealApi
    ): RemoteMealDataSource = RemoteMealDataSourceImpl(api)

    @Provides
    fun provideRemoteMealRepository(
        remoteMealDataSource: RemoteMealDataSource
    ): RemoteMealRepository = RemoteMealRepository(remoteMealDataSource)
}