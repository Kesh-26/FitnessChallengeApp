package com.devarsh.fitnesschallengeapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_badges")
data class UserBadge(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val badgeId: Int,
    val unlockedDate: String // ISO-8601
)