package com.practice.meal.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MealRoomModule {

    @Singleton
    @Provides
    fun provideMealDatabase(
        @ApplicationContext context: Context
    ): com.practice.meal.room.MealDatabase = Room.databaseBuilder(
        context,
        com.practice.meal.room.MealDatabase::class.java,
        "meal-database"
    ).build()

    @Provides
    fun provideMealDao(mealDatabase: com.practice.meal.room.MealDatabase): com.practice.meal.room.MealDao = mealDatabase.mealDao()

}