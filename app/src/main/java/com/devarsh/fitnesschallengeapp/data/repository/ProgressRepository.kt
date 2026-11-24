package com.devarsh.fitnesschallengeapp.data.repository

import com.devarsh.fitnesschallengeapp.data.dao.ProgressDao
import com.devarsh.fitnesschallengeapp.data.entity.Progress
import kotlinx.coroutines.flow.Flow

class ProgressRepository(private val dao: ProgressDao) {
    fun getByChallenge(challengeId: Int): Flow<List<Progress>> = dao.getByChallenge(challengeId)
    fun getByDate(date: String): Flow<List<Progress>> = dao.getByDate(date)
    fun getByDateRange(startDate: String, endDate: String): Flow<List<Progress>> =
        dao.getByDateRange(startDate, endDate)
    val totalCompleted: Flow<Int> = dao.getTotalCompletedCount()

    suspend fun insert(progress: Progress) = dao.insert(progress)
    suspend fun update(progress: Progress) = dao.update(progress)
    suspend fun getActiveDaysInRange(start: String, end: String): Int = dao.getActiveDaysInRange(start, end)
}