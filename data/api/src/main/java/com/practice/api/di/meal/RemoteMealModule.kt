package com.practice.api.di.meal

import com.practice.api.meal.RemoteMealDataSource
import com.practice.api.meal.RemoteMealDataSourceImpl
import com.practice.api.meal.RemoteMealRepository
import com.practice.api.meal.api.MealApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteMealModule {

    @Singleton
    @Provides
    fun provideRemoteMealDataSource(
        api: MealApi
    ): RemoteMealDataSource = RemoteMealDataSourceImpl(api)

    @Singleton
    @Provides
    fun provideRemoteMealRepository(
        remoteMealDataSource: RemoteMealDataSource
    ) = RemoteMealRepository(remoteMealDataSource)
}