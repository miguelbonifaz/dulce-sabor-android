package com.dulcesabor.android.data

import com.dulcesabor.android.auth.AuthRepository
import com.dulcesabor.android.auth.InvalidCredentialsException
import com.dulcesabor.android.auth.StaffUser
import kotlinx.coroutines.delay

/** A local simulation only. These public demo credentials do not authenticate with Laravel. */
class DemoAuthRepository : AuthRepository {
    override suspend fun signIn(email: String, password: String): StaffUser {
        delay(650)
        if (!email.equals(EMAIL, ignoreCase = true) || password != PASSWORD) {
            throw InvalidCredentialsException()
        }
        return StaffUser(name = "Equipo Dulce Sabor", email = EMAIL)
    }

    companion object {
        const val EMAIL = "equipo@dulcesabor.demo"
        const val PASSWORD = "DulceSabor2026"
    }
}
