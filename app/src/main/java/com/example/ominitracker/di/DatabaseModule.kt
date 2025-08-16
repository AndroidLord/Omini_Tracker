package com.example.ominitracker.di

import android.content.Context
import androidx.room.Room
import com.example.ominitracker.data.AppDatabase
import com.example.ominitracker.data.dao.HabitDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, AppDatabase.DATABASE_NAME).build()
    }

    @Provides
    fun provideDao(database: AppDatabase): HabitDao {
        return database.habitDao()
    }

}