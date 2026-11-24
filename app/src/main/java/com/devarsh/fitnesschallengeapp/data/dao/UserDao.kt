package com.devarsh.fitnesschallengeapp.data.dao


import androidx.room.*
import com.devarsh.fitnesschallengeapp.data.entity.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE id = 1")
    fun getUser(): Flow<User?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)

    @Update
    suspend fun update(user: User)

    @Query("DELETE FROM users")
    suspend fun deleteAll()
}