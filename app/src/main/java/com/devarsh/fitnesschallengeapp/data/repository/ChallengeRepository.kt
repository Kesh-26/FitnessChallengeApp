package com.devarsh.fitnesschallengeapp.data.repository

import com.devarsh.fitnesschallengeapp.data.dao.ChallengeDao
import com.devarsh.fitnesschallengeapp.data.entity.Challenge
import kotlinx.coroutines.flow.Flow

class ChallengeRepository(private val dao: ChallengeDao) {
    val allChallenges: Flow<List<Challenge>> = dao.getAll()

    fun getByCategory(category: String): Flow<List<Challenge>> = dao.getByCategory(category)

    suspend fun getById(id: Int): Challenge? = dao.getById(id)

    suspend fun seedChallenges(challenges: List<Challenge>) {
        if (dao.count() == 0) {
            dao.insertAll(challenges)
        }
    }
}