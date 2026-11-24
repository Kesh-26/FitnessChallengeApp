package com.devarsh.fitnesschallengeapp.data.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.devarsh.fitnesschallengeapp.data.entity.Settings
import com.devarsh.fitnesschallengeapp.data.repository.SettingsRepository
import com.devarsh.fitnesschallengeapp.notification.ReminderScheduler
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    application: Application,
    private val repository: SettingsRepository
) : AndroidViewModel(application) {

    val settings: StateFlow<Settings?> =
        repository.settings.stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            null
        )

    fun updateReminder(
        enabled: Boolean,
        time: String
    ) {
        viewModelScope.launch {
            val current = settings.value ?: Settings()

            repository.update(
                current.copy(
                    reminderEnabled = enabled,
                    reminderTime = time
                )
            )

            ReminderScheduler.schedule(
                getApplication<Application>().applicationContext,
                time,
                enabled
            )
        }
    }

    fun updateDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            val current = settings.value ?: Settings()

            repository.update(
                current.copy(darkMode = enabled)
            )
        }
    }

    fun updateUnits(units: String) {
        viewModelScope.launch {
            val current = settings.value ?: Settings()

            repository.update(
                current.copy(units = units)
            )
        }
    }

    fun saveInitial() {
        viewModelScope.launch {
            if (settings.value == null) {
                repository.save(Settings())
            }
        }
    }
}