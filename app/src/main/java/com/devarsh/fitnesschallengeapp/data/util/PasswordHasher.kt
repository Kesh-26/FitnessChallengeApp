package com.devarsh.fitnesschallengeapp.data.util



import java.security.MessageDigest

object PasswordHasher {
    fun hash(password: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray(Charsets.UTF_8))
        return bytes.joinToString("") { "%02x".format(it) }
    }

    fun verify(password: String, hash: String): Boolean {
        return hash(password) == hash
    }
}