package com.dulcesabor.android.auth

data class StaffUser(val name: String, val email: String)

interface AuthRepository {
    suspend fun signIn(email: String, password: String): StaffUser
}

class InvalidCredentialsException : Exception()

object LoginValidation {
    private val emailPattern = Regex("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")

    fun isValidEmail(value: String): Boolean = emailPattern.matches(value.trim())
}
