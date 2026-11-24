package com.devarsh.fitnesschallengeapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_steps")
data class DailySteps(
    @PrimaryKey
    val date: String,

    val sensorBaseline: Int,
    val currentSensorValue: Int,
    val stepsToday: Int
)