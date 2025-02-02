package com.example.ominitracker.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.ominitracker.data.modal.HabitEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {

    @Query("SELECT * FROM habit")
    fun getAllHabits(): Flow<List<HabitEntity>>

    @Query("SELECT * FROM habit WHERE id = :id")
    suspend fun getHabitById(id: Int): HabitEntity

    @Upsert
    suspend fun upsertHabit(habit: HabitEntity)

    @Delete
    suspend fun deleteHabit(habit: HabitEntity)
}