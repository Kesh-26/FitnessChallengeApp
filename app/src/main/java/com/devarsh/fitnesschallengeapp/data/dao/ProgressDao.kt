package com.devarsh.fitnesschallengeapp.data.dao

import androidx.room.*
import com.devarsh.fitnesschallengeapp.data.entity.Progress
import kotlinx.coroutines.flow.Flow

@Dao
interface ProgressDao {
    @Query("SELECT * FROM progress WHERE challengeId = :challengeId ORDER BY date DESC")
    fun getByChallenge(challengeId: Int): Flow<List<Progress>>

    @Query("SELECT * FROM progress WHERE date = :date")
    fun getByDate(date: String): Flow<List<Progress>>

    @Query("SELECT * FROM progress WHERE completed = 1")
    fun getCompleted(): Flow<List<Progress>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(progress: Progress)

    @Update
    suspend fun update(progress: Progress)

    @Query("SELECT COUNT(*) FROM progress WHERE completed = 1")
    fun getTotalCompletedCount(): Flow<Int>

    @Query("""
        SELECT COUNT(DISTINCT date) FROM progress 
        WHERE completed = 1 AND date >= :startDate AND date <= :endDate
    """)
    suspend fun getActiveDaysInRange(startDate: String, endDate: String): Int

    @Query("SELECT * FROM progress WHERE date >= :startDate AND date <= :endDate ORDER BY date DESC")
    fun getByDateRange(startDate: String, endDate: String): Flow<List<Progress>>
}