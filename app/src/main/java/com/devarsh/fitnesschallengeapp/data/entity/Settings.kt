package com.devarsh.fitnesschallengeapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "settings")
data class Settings(
    @PrimaryKey val id: Int = 1,
    val reminderEnabled: Boolean = true,
    val reminderTime: String = "08:00", // HH:mm
    val darkMode: Boolean = false,
    val units: String = "metric" // metric or imperial
)
