package com.devarsh.fitnesschallengeapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "badges")
data class Badge(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val description: String,
    val requirementType: String, // TOTAL_COMPLETED, STREAK_DAYS, CATEGORY_SPECIFIC
    val requirementValue: Int,
    val categoryFilter: String? = null
)