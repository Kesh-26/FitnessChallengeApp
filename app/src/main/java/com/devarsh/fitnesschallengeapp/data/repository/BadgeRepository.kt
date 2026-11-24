package com.devarsh.fitnesschallengeapp.data.repository

import com.devarsh.fitnesschallengeapp.data.dao.BadgeDao
import com.devarsh.fitnesschallengeapp.data.entity.Badge
import com.devarsh.fitnesschallengeapp.data.entity.UserBadge
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class BadgeRepository(private val dao: BadgeDao) {

    val allBadges: Flow<List<Badge>> = dao.getAllBadges()
    val unlockedBadges: Flow<List<Badge>> = dao.getUnlockedBadges()

    suspend fun seedBadges(badges: List<Badge>) {
        if (dao.getAllBadges().first().isEmpty()) {
            dao.insertAll(badges)
        }
    }

    suspend fun unlockBadge(userBadge: UserBadge) =
        dao.unlockBadge(userBadge)

    suspend fun isUnlocked(badgeId: Int): Boolean =
        dao.isUnlocked(badgeId) > 0
}