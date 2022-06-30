package com.practice.di.meal

import com.practice.neis.common.NeisApi
import com.practice.neis.meal.retrofit.MealApi
import com.practice.neis.meal.retrofit.mealApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object MealApiModule {

    @Provides
    fun provideMealServiceDietInfo(): MealApi = NeisApi.mealApi
}