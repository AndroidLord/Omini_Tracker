package com.example.ominitracker.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.ominitracker.data.dao.HabitDao
import com.example.ominitracker.data.modal.HabitEntity

@Database(entities = [HabitEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun habitDao(): HabitDao

    companion object {
        const val DATABASE_NAME = "OmniDB"
    }
}