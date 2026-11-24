package com.devarsh.fitnesschallengeapp.data.util



import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import java.util.UUID

class AuthManager(context: Context) {

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val prefs: SharedPreferences = EncryptedSharedPreferences.create(
        context,
        "auth_secure",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun register(username: String, password: String): Boolean {
        if (username.isBlank() || password.length < 6) return false
        if (prefs.contains(key(username))) return false // Username taken

        val hash = PasswordHasher.hash(password)
        prefs.edit()
            .putString(key(username), hash)
            .apply()
        return true
    }

    fun login(username: String, password: String): Boolean {
        val storedHash = prefs.getString(key(username), null) ?: return false
        if (!PasswordHasher.verify(password, storedHash)) return false

        val token = UUID.randomUUID().toString()
        prefs.edit()
            .putString("session_token", token)
            .putString("current_user", username)
            .putBoolean("is_logged_in", true)
            .apply()
        return true
    }

    fun logout() {
        prefs.edit()
            .remove("session_token")
            .remove("current_user")
            .putBoolean("is_logged_in", false)
            .apply()
    }

    fun isLoggedIn(): Boolean {
        return prefs.getBoolean("is_logged_in", false) &&
                prefs.getString("session_token", null) != null
    }

    fun getCurrentUsername(): String? = prefs.getString("current_user", null)

    private fun key(username: String) = "pwd_$username"
}