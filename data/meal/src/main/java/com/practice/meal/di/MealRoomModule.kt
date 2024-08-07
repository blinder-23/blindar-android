package com.practice.meal.di

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
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
    ).addMigrations(MIGRATION12, MIGRATION23)
        .build()

    private val MIGRATION12 = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("CREATE TABLE IF NOT EXISTS meal_migration (date TEXT NOT NULL, menu TEXT NOT NULL, origin TEXT NOT NULL, calorie REAL NOT NULL, nutrient TEXT NOT NULL, school_code INTEGER NOT NULL, PRIMARY KEY(school_code, date))")
            db.execSQL("INSERT INTO meal_migration (date, menu, origin, calorie, nutrient, school_code) SELECT date, menu, origin, calorie, nutrient, school_code FROM meal")
            db.execSQL("DROP TABLE meal")
            db.execSQL("ALTER TABLE meal_migration RENAME TO meal")
        }
    }

    private val MIGRATION23 = object : Migration(2, 3) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("DROP TABLE meal")
            db.execSQL(
                """
                CREATE TABLE IF NOT EXISTS meal (
                    date TEXT NOT NULL, 
                    menu TEXT NOT NULL, 
                    origin TEXT NOT NULL, 
                    calorie REAL NOT NULL, 
                    nutrient TEXT NOT NULL, 
                    meal_time TEXT NOT NULL,
                    school_code INTEGER NOT NULL, 
                    PRIMARY KEY(school_code, date, meal_time)
                )
                """
                    .trimIndent()
            )
        }
    }

    @Provides
    fun provideMealDao(mealDatabase: MealDatabase): MealDao = mealDatabase.mealDao()

}