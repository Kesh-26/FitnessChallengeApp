package com.devarsh.fitnesschallengeapp.data.repository

import com.devarsh.fitnesschallengeapp.data.dao.DailyStepsDao
import com.devarsh.fitnesschallengeapp.data.entity.DailySteps
import kotlinx.coroutines.flow.Flow

class DailyStepsRepository(
    private val dailyStepsDao: DailyStepsDao
) {

    fun getStepsForDate(date: String): Flow<DailySteps?> {
        return dailyStepsDao.getForDate(date)
    }

    suspend fun getStepsForDateOnce(date: String): DailySteps? {
        return dailyStepsDao.getForDateOnce(date)
    }

    suspend fun saveSteps(dailySteps: DailySteps) {
        dailyStepsDao.save(dailySteps)
    }

    suspend fun deleteOlderThan(date: String) {
        dailyStepsDao.deleteOlderThan(date)
    }
}