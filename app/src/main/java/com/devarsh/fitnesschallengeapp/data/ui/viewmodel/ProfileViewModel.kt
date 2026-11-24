package com.devarsh.fitnesschallengeapp.data.ui.viewmodel

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devarsh.fitnesschallengeapp.data.entity.User
import com.devarsh.fitnesschallengeapp.data.repository.UserRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.File

class ProfileViewModel(private val repository: UserRepository) : ViewModel() {
    val user: StateFlow<User?> = repository.user.stateIn(viewModelScope, SharingStarted.Lazily, null)

    fun saveProfile(name: String, age: Int, heightCm: Int, weightKg: Float, goal: String) {
        viewModelScope.launch {
            val current = user.value
            val updated = current?.copy(
                name = name,
                age = age,
                heightCm = heightCm,
                weightKg = weightKg,
                goal = goal
            ) ?: User(
                name = name,
                age = age,
                heightCm = heightCm,
                weightKg = weightKg,
                goal = goal
            )
            repository.save(updated)
        }
    }

    fun updateProfile(name: String, age: Int, heightCm: Int, weightKg: Float, goal: String) {
        viewModelScope.launch {
            val current = user.value
            val updated = current?.copy(
                name = name,
                age = age,
                heightCm = heightCm,
                weightKg = weightKg,
                goal = goal
            ) ?: User(
                name = name,
                age = age,
                heightCm = heightCm,
                weightKg = weightKg,
                goal = goal
            )
            repository.update(updated)
        }
    }

    fun saveProfilePhoto(bitmap: Bitmap, context: Context) {
        viewModelScope.launch {
            try {
                val filename = "profile_${System.currentTimeMillis()}.jpg"
                context.openFileOutput(filename, Context.MODE_PRIVATE).use { out ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
                }
                val path = File(context.filesDir, filename).absolutePath
                user.value?.let {
                    repository.update(it.copy(photoPath = path))
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun calculateBMI(heightCm: Int, weightKg: Float): Float {
        val heightM = heightCm / 100f
        return weightKg / (heightM * heightM)
    }
}