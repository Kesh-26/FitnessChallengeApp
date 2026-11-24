package com.devarsh.fitnesschallengeapp.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "progress",
    foreignKeys = [
        ForeignKey(
            entity = Challenge::class,
            parentColumns = ["id"],
            childColumns = ["challengeId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Progress(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val challengeId: Int,
    val date: String, // ISO-8601 LocalDate
    val value: Int,
    val completed: Boolean = false
)