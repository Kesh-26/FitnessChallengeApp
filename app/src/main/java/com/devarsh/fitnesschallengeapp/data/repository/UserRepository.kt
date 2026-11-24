package com.devarsh.fitnesschallengeapp.data.repository

import com.devarsh.fitnesschallengeapp.data.dao.UserDao
import com.devarsh.fitnesschallengeapp.data.entity.User
import kotlinx.coroutines.flow.Flow

class UserRepository(private val dao: UserDao) {
    val user: Flow<User?> = dao.getUser()

    suspend fun save(user: User) = dao.insert(user)
    suspend fun update(user: User) = dao.update(user)
    suspend fun deleteAll() = dao.deleteAll()
}