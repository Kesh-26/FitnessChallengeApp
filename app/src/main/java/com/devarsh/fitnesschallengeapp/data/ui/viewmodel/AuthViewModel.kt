package com.devarsh.fitnesschallengeapp.data.ui.viewmodel



import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devarsh.fitnesschallengeapp.data.entity.User
import com.devarsh.fitnesschallengeapp.data.repository.UserRepository
import com.devarsh.fitnesschallengeapp.data.util.AuthManager
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authManager: AuthManager,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    sealed class AuthState {
        object Idle : AuthState()
        object Loading : AuthState()
        object Success : AuthState()
        data class Error(val message: String) : AuthState()
    }

    fun register(username: String, password: String, confirmPassword: String) {
        if (password != confirmPassword) {
            _authState.value = AuthState.Error("Passwords do not match")
            return
        }
        if (password.length < 6) {
            _authState.value = AuthState.Error("Password must be at least 6 characters")
            return
        }

        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val success = authManager.register(username, password)
            _authState.value = if (success) {
                // Create empty user profile linked to username
                userRepository.save(User(username = username, name = "", age = 0, heightCm = 0, weightKg = 0f, goal = ""))
                AuthState.Success
            } else {
                AuthState.Error("Username already exists")
            }
        }
    }

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val success = authManager.login(username, password)
            _authState.value = if (success) AuthState.Success
            else AuthState.Error("Invalid username or password")
        }
    }

    fun logout() {
        authManager.logout()
        _authState.value = AuthState.Idle
    }

    fun resetState() {
        _authState.value = AuthState.Idle
    }

    fun isLoggedIn(): Boolean = authManager.isLoggedIn()
    fun getCurrentUser(): String? = authManager.getCurrentUsername()
}