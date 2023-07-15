package com.practice.meal.di

import android.content.Context
import androidx.room.Room
import com.practice.meal.room.MealDao
import com.practice.meal.room.MealDatabase
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
    ): MealDatabase = Room.databaseBuilder(
        context,
        MealDatabase::class.java,
        "meal-database"
    ).build()

    @Provides
    fun provideMealDao(mealDatabase: MealDatabase): MealDao = mealDatabase.mealDao()

}