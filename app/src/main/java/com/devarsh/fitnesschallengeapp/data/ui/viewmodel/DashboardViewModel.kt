package com.devarsh.fitnesschallengeapp.data.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devarsh.fitnesschallengeapp.data.entity.User
import com.devarsh.fitnesschallengeapp.data.repository.BadgeRepository
import com.devarsh.fitnesschallengeapp.data.repository.ProgressRepository
import com.devarsh.fitnesschallengeapp.data.repository.UserRepository
import com.devarsh.fitnesschallengeapp.data.util.StreakCalculator
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate

class DashboardViewModel(
    userRepo: UserRepository,
    private val progressRepo: ProgressRepository,
    private val badgeRepo: BadgeRepository,
    private val streakCalculator: StreakCalculator
) : ViewModel() {

    val user: StateFlow<User?> =
        userRepo.user.stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            null
        )

    val totalPoints: StateFlow<Int> =
        progressRepo.totalCompleted
            .map { it * 10 }
            .stateIn(
                viewModelScope,
                SharingStarted.Lazily,
                0
            )

    val totalCompleted: StateFlow<Int> =
        progressRepo.totalCompleted
            .stateIn(
                viewModelScope,
                SharingStarted.Lazily,
                0
            )

    val badgeCount: StateFlow<Int> =
        badgeRepo.unlockedBadges
            .map { it.size }
            .stateIn(
                viewModelScope,
                SharingStarted.Lazily,
                0
            )

    val currentStreak: StateFlow<Int> =
        flow {
            emit(streakCalculator.getCurrentStreak())
        }.stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            0
        )

    val longestStreak: StateFlow<Int> =
        flow {
            emit(streakCalculator.getLongestStreak())
        }.stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            0
        )

    val weeklyActivity: StateFlow<Map<String, Int>> =
        flow {
            val today = LocalDate.now()
            val start = today.minusDays(6)

            val list = progressRepo
                .getByDateRange(
                    start.toString(),
                    today.toString()
                )
                .first()

            val activity = list
                .filter { it.completed }
                .groupBy { it.date }
                .mapValues { it.value.size }

            emit(activity)
        }.stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            emptyMap()
        )
}