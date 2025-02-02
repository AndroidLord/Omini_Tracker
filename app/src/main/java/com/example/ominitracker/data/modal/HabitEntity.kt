package com.example.ominitracker.data.modal

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habit")
data class HabitEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val frequency: String,
    val customInterval: String,
    val repetitionCount: String,
    val cueTrigger: String,
    val goalCount: Int,
    val reward: String,
    val isComplete : Boolean = false
)