package com.devarsh.fitnesschallengeapp.data.repository

import com.devarsh.fitnesschallengeapp.data.dao.SettingsDao
import com.devarsh.fitnesschallengeapp.data.entity.Settings
import kotlinx.coroutines.flow.Flow

class SettingsRepository(private val dao: SettingsDao) {
    val settings: Flow<Settings?> = dao.getSettings()

    suspend fun save(settings: Settings) = dao.insert(settings)
    suspend fun update(settings: Settings) = dao.update(settings)
}
