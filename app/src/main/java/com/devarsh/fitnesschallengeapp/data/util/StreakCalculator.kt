package com.devarsh.fitnesschallengeapp.data.util

import com.devarsh.fitnesschallengeapp.data.dao.ProgressDao
import kotlinx.coroutines.flow.first
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class StreakCalculator(private val progressDao: ProgressDao) {

    suspend fun getCurrentStreak(): Int {
        val completed = progressDao.getCompleted().first()
        if (completed.isEmpty()) return 0

        val dates = completed.map { LocalDate.parse(it.date) }.distinct().sortedDescending()
        val today = LocalDate.now()
        val daysSinceLast = ChronoUnit.DAYS.between(dates.first(), today)

        if (daysSinceLast > 1) return 0

        var streak = 0
        var expected = today.minusDays(daysSinceLast)

        for (date in dates) {
            if (date == expected) {
                streak++
                expected = date.minusDays(1)
            } else break
        }
        return streak
    }

    suspend fun getLongestStreak(): Int {
        val completed = progressDao.getCompleted().first()
        if (completed.isEmpty()) return 0

        val dates = completed.map { LocalDate.parse(it.date) }.distinct().sorted()
        var longest = 1
        var current = 1

        for (i in 1 until dates.size) {
            val prev = dates[i - 1]
            val curr = dates[i]
            if (ChronoUnit.DAYS.between(prev, curr) == 1L) {
                current++
                if (current > longest) longest = current
            } else if (prev != curr) {
                current = 1
            }
        }
        return longest
    }
}