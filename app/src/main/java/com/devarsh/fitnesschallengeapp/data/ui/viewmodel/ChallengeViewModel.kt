package com.devarsh.fitnesschallengeapp.data.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devarsh.fitnesschallengeapp.data.entity.Badge
import com.devarsh.fitnesschallengeapp.data.entity.Challenge
import com.devarsh.fitnesschallengeapp.data.repository.BadgeRepository
import com.devarsh.fitnesschallengeapp.data.repository.ChallengeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ChallengeViewModel(
    private val repository: ChallengeRepository,
    private val badgeRepo: BadgeRepository
) : ViewModel() {

    val challenges: StateFlow<List<Challenge>> =
        repository.allChallenges.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = emptyList()
        )

    private val _selectedCategory = MutableStateFlow("ALL")

    val selectedCategory: StateFlow<String> =
        _selectedCategory.asStateFlow()

    fun setCategory(category: String) {
        _selectedCategory.value = category
    }

    fun seedData() {
        viewModelScope.launch {
            val defaultChallenges = listOf(
                Challenge(
                    title = "Morning Walk",
                    category = "WALKING",
                    target = 3000,
                    unit = "steps",
                    difficulty = "EASY",
                    points = 10,
                    description = "Walk 3,000 steps"
                ),
                Challenge(
                    title = "Power Walk",
                    category = "WALKING",
                    target = 5000,
                    unit = "steps",
                    difficulty = "MEDIUM",
                    points = 20,
                    description = "Walk 5,000 steps"
                ),
                Challenge(
                    title = "Long March",
                    category = "WALKING",
                    target = 10000,
                    unit = "steps",
                    difficulty = "HARD",
                    points = 50,
                    description = "Walk 10,000 steps"
                ),
                Challenge(
                    title = "Push-Up Beginner",
                    category = "STRENGTH",
                    target = 10,
                    unit = "reps",
                    difficulty = "EASY",
                    points = 10,
                    description = "Complete 10 push-ups"
                ),
                Challenge(
                    title = "Squat Master",
                    category = "STRENGTH",
                    target = 50,
                    unit = "reps",
                    difficulty = "MEDIUM",
                    points = 25,
                    description = "Complete 50 squats"
                ),
                Challenge(
                    title = "Plank Hold",
                    category = "STRENGTH",
                    target = 60,
                    unit = "seconds",
                    difficulty = "MEDIUM",
                    points = 20,
                    description = "Hold plank for 60 seconds"
                ),
                Challenge(
                    title = "Stretching",
                    category = "FLEXIBILITY",
                    target = 15,
                    unit = "minutes",
                    difficulty = "EASY",
                    points = 10,
                    description = "Stretch for 15 minutes"
                ),
                Challenge(
                    title = "Yoga Flow",
                    category = "FLEXIBILITY",
                    target = 30,
                    unit = "minutes",
                    difficulty = "MEDIUM",
                    points = 25,
                    description = "Complete 30 min yoga"
                ),
                Challenge(
                    title = "Hydration",
                    category = "HABIT",
                    target = 8,
                    unit = "glasses",
                    difficulty = "EASY",
                    points = 10,
                    description = "Drink 8 glasses of water"
                ),
                Challenge(
                    title = "Sleep Well",
                    category = "HABIT",
                    target = 8,
                    unit = "hours",
                    difficulty = "EASY",
                    points = 10,
                    description = "Sleep 8 hours",
                    isWeekly = true
                ),
                Challenge(
                    title = "Move Break",
                    category = "HABIT",
                    target = 5,
                    unit = "breaks",
                    difficulty = "EASY",
                    points = 15,
                    description = "Take 5 movement breaks",
                    isStreak = true
                )
            )

            repository.seedChallenges(defaultChallenges)
        }
    }

    fun seedBadges() {
        viewModelScope.launch {
            val badges: List<Badge> = listOf(
                Badge(
                    name = "Beginner",
                    description = "Complete 5 challenges",
                    requirementType = "TOTAL_COMPLETED",
                    requirementValue = 5
                ),
                Badge(
                    name = "Intermediate",
                    description = "Complete 20 challenges",
                    requirementType = "TOTAL_COMPLETED",
                    requirementValue = 20
                ),
                Badge(
                    name = "Expert",
                    description = "Complete 50 challenges",
                    requirementType = "TOTAL_COMPLETED",
                    requirementValue = 50
                ),
                Badge(
                    name = "Week Warrior",
                    description = "7 day streak",
                    requirementType = "STREAK_DAYS",
                    requirementValue = 7
                ),
                Badge(
                    name = "Month Master",
                    description = "30 day streak",
                    requirementType = "STREAK_DAYS",
                    requirementValue = 30
                ),
                Badge(
                    name = "Century Club",
                    description = "100 day streak",
                    requirementType = "STREAK_DAYS",
                    requirementValue = 100
                ),
                Badge(
                    name = "Walker",
                    description = "10 walking challenges",
                    requirementType = "CATEGORY_SPECIFIC",
                    requirementValue = 10,
                    categoryFilter = "WALKING"
                ),
                Badge(
                    name = "Strength Builder",
                    description = "10 strength challenges",
                    requirementType = "CATEGORY_SPECIFIC",
                    requirementValue = 10,
                    categoryFilter = "STRENGTH"
                ),
                Badge(
                    name = "Flexi Friend",
                    description = "10 flexibility challenges",
                    requirementType = "CATEGORY_SPECIFIC",
                    requirementValue = 10,
                    categoryFilter = "FLEXIBILITY"
                ),
                Badge(
                    name = "Hydration Hero",
                    description = "10 habit challenges",
                    requirementType = "CATEGORY_SPECIFIC",
                    requirementValue = 10,
                    categoryFilter = "HABIT"
                )
            )

            badgeRepo.seedBadges(badges)
        }
    }
}