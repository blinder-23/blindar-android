package com.example.server.di.meal

import com.example.server.meal.RemoteMealDataSource
import com.example.server.meal.RemoteMealDataSourceImpl
import com.example.server.meal.RemoteMealRepository
import com.example.server.meal.api.MealApi
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