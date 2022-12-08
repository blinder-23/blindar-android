package com.example.server.di.meal

import com.example.server.meal.api.MealApi
import com.example.server.meal.api.mealApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MealApiModule {

    @Singleton
    @Provides
    fun provideMealApi(): MealApi = mealApi
}