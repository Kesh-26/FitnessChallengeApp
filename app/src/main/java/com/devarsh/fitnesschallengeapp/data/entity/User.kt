package com.devarsh.fitnesschallengeapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey val id: Int = 1,
    val username: String = "",      // NEW
    val name: String,
    val age: Int,
    val heightCm: Int,
    val weightKg: Float,
    val goal: String,
    val photoPath: String? = null   // NEW
)