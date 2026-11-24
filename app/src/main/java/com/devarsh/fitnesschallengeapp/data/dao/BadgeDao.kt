package com.devarsh.fitnesschallengeapp.data.dao

import androidx.room.*
import com.devarsh.fitnesschallengeapp.data.entity.Badge
import com.devarsh.fitnesschallengeapp.data.entity.UserBadge
import kotlinx.coroutines.flow.Flow

@Dao
interface BadgeDao {
    @Query("SELECT * FROM badges")
    fun getAllBadges(): Flow<List<Badge>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(badges: List<Badge>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun unlockBadge(userBadge: UserBadge)

    @Query("""
        SELECT b.* FROM badges b 
        INNER JOIN user_badges ub ON b.id = ub.badgeId 
        ORDER BY ub.unlockedDate DESC
    """)
    fun getUnlockedBadges(): Flow<List<Badge>>

    @Query("SELECT COUNT(*) FROM user_badges WHERE badgeId = :badgeId")
    suspend fun isUnlocked(badgeId: Int): Int
}