package com.practice.api.di.meal

import com.practice.api.meal.api.MealApi
import com.practice.api.meal.api.mealApi
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