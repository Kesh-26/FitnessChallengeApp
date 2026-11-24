package com.devarsh.fitnesschallengeapp.data.engine

import com.devarsh.fitnesschallengeapp.data.dao.BadgeDao
import com.devarsh.fitnesschallengeapp.data.dao.ChallengeDao
import com.devarsh.fitnesschallengeapp.data.dao.ProgressDao
import com.devarsh.fitnesschallengeapp.data.entity.UserBadge
import com.devarsh.fitnesschallengeapp.data.util.StreakCalculator
import kotlinx.coroutines.flow.first
import java.time.LocalDate

class BadgeEngine(
    private val badgeDao: BadgeDao,
    private val progressDao: ProgressDao,
    private val challengeDao: ChallengeDao,
    private val streakCalculator: StreakCalculator
) {

    suspend fun evaluateAndUnlock() {
        val badges = badgeDao.getAllBadges().first()
        val totalCompleted = progressDao.getTotalCompletedCount().first()
        val currentStreak = streakCalculator.getCurrentStreak()

        val challenges = challengeDao.getAll().first()
        val categoryMap = challenges.associate { it.id to it.category }

        val categoryCounts = progressDao.getCompleted().first()
            .mapNotNull { progress ->
                categoryMap[progress.challengeId]?.let { category ->
                    category to progress
                }
            }
            .groupingBy { it.first }
            .eachCount()

        for (badge in badges) {

            // isUnlocked() returns Int, so convert it to a Boolean condition
            if (badgeDao.isUnlocked(badge.id) > 0) {
                continue
            }

            val shouldUnlock = when (badge.requirementType) {
                "TOTAL_COMPLETED" ->
                    totalCompleted >= badge.requirementValue

                "STREAK_DAYS" ->
                    currentStreak >= badge.requirementValue

                "CATEGORY_SPECIFIC" ->
                    (categoryCounts[badge.categoryFilter] ?: 0) >= badge.requirementValue

                else -> false
            }

            if (shouldUnlock) {
                badgeDao.unlockBadge(
                    UserBadge(
                        badgeId = badge.id,
                        unlockedDate = LocalDate.now().toString()
                    )
                )
            }
        }
    }
}