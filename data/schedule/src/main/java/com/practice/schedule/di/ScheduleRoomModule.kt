package com.practice.schedule.di

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.practice.schedule.room.ScheduleDao
import com.practice.schedule.room.ScheduleDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ScheduleRoomModule {

    @Singleton
    @Provides
    fun provideScheduleDatabase(@ApplicationContext context: Context): ScheduleDatabase =
        Room.databaseBuilder(
            context,
            ScheduleDatabase::class.java,
            "schedule-database"
        ).addMigrations(MIGRATION12)
            .build()

    private val MIGRATION12 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("CREATE TABLE IF NOT EXISTS schedule_migration (school_code INTEGER NOT NULL DEFAULT 7010578, id INTEGER NOT NULL, date TEXT NOT NULL, event_name TEXT NOT NULL, event_content TEXT NOT NULL, PRIMARY KEY(school_code, id))")
            database.execSQL("INSERT INTO schedule_migration (id, date, event_name, event_content) SELECT id, date, event_name, event_content FROM schedule")
            database.execSQL("DROP TABLE schedule")
            database.execSQL("ALTER TABLE schedule_migration RENAME TO schedule")
        }
    }

    @Provides
    fun provideScheduleDao(
        scheduleDatabase: ScheduleDatabase
    ): ScheduleDao = scheduleDatabase.scheduleDao()
}