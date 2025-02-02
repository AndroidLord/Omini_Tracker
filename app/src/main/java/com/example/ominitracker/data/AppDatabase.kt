package com.example.ominitracker.data

import androidx.room.RoomDatabase
import com.example.ominitracker.data.dao.HabitDao

abstract class AppDatabase: RoomDatabase() {
    abstract fun habitDao(): HabitDao

    companion object {
        const val DATABASE_NAME = "habit_tracker_db"
    }



}