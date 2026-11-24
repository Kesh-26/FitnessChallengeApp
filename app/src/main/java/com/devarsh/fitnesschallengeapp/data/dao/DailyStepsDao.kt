package com.devarsh.fitnesschallengeapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.devarsh.fitnesschallengeapp.data.entity.DailySteps
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyStepsDao {

    @Query("SELECT * FROM daily_steps WHERE date = :date LIMIT 1")
    fun getForDate(date: String): Flow<DailySteps?>

    @Query("SELECT * FROM daily_steps WHERE date = :date LIMIT 1")
    suspend fun getForDateOnce(date: String): DailySteps?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(dailySteps: DailySteps)

    @Query("DELETE FROM daily_steps WHERE date < :beforeDate")
    suspend fun deleteOlderThan(beforeDate: String)
}