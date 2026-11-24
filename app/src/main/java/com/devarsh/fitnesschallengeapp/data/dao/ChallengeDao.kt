package com.devarsh.fitnesschallengeapp.data.dao

import androidx.room.*
import com.devarsh.fitnesschallengeapp.data.entity.Challenge
import kotlinx.coroutines.flow.Flow

@Dao
interface ChallengeDao {
    @Query("SELECT * FROM challenges ORDER BY category, difficulty")
    fun getAll(): Flow<List<Challenge>>

    @Query("SELECT * FROM challenges WHERE category = :category")
    fun getByCategory(category: String): Flow<List<Challenge>>

    @Query("SELECT * FROM challenges WHERE id = :id")
    suspend fun getById(id: Int): Challenge?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(challenges: List<Challenge>)

    @Query("SELECT COUNT(*) FROM challenges")
    suspend fun count(): Int
}