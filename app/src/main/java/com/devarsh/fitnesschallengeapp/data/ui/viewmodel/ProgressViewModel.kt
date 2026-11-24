package com.devarsh.fitnesschallengeapp.data.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.devarsh.fitnesschallengeapp.data.database.DatabaseProvider
import com.devarsh.fitnesschallengeapp.data.engine.BadgeEngine
import com.devarsh.fitnesschallengeapp.data.entity.Progress
import com.devarsh.fitnesschallengeapp.data.repository.BadgeRepository
import com.devarsh.fitnesschallengeapp.data.repository.ChallengeRepository
import com.devarsh.fitnesschallengeapp.data.repository.ProgressRepository
import com.devarsh.fitnesschallengeapp.data.util.StreakCalculator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

class ProgressViewModel(
    application: Application,
    private val progressRepo: ProgressRepository,
    private val badgeRepo: BadgeRepository,
    private val challengeRepo: ChallengeRepository
) : AndroidViewModel(application) {

    private val db = DatabaseProvider.getDatabase(application)

    private val streakCalculator =
        StreakCalculator(db.progressDao())

    private val badgeEngine = BadgeEngine(
        badgeDao = db.badgeDao(),
        progressDao = db.progressDao(),
        challengeDao = db.challengeDao(),
        streakCalculator = streakCalculator
    )

    fun getProgressForChallenge(
        challengeId: Int
    ): Flow<List<Progress>> =
        progressRepo.getByChallenge(challengeId)

    suspend fun getChallengeById(id: Int) =
        challengeRepo.getById(id)

    fun recordProgress(
        challengeId: Int,
        value: Int,
        target: Int
    ) {
        viewModelScope.launch {
            val today = LocalDate.now().toString()
            val completed = value >= target

            progressRepo.insert(
                Progress(
                    challengeId = challengeId,
                    date = today,
                    value = value,
                    completed = completed
                )
            )

            badgeEngine.evaluateAndUnlock()
        }
    }

    val totalCompleted: StateFlow<Int> =
        progressRepo.totalCompleted.stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            0
        )
}