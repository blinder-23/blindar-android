package com.practice.di.meal

import android.content.Context
import androidx.room.Room
import com.practice.database.meal.room.MealDao
import com.practice.database.meal.room.MealDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object MealRoomModule {

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