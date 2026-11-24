package com.devarsh.fitnesschallengeapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "challenges")
data class Challenge(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val category: String, // WALKING, STRENGTH, FLEXIBILITY, HABIT
    val target: Int,
    val unit: String, // steps, reps, minutes, glasses
    val difficulty: String, // EASY, MEDIUM, HARD
    val points: Int,
    val description: String,
    val isWeekly: Boolean = false,
    val isStreak: Boolean = false
)