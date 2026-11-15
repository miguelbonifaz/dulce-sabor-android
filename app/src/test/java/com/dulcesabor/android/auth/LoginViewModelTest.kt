package com.dulcesabor.android.auth

import com.dulcesabor.android.data.DemoAuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {
    private val dispatcher = StandardTestDispatcher()

    @Before fun setUp() { Dispatchers.setMain(dispatcher) }
    @After fun tearDown() { Dispatchers.resetMain() }

    @Test fun `invalid input never calls authentication`() = runTest(dispatcher) {
        var called = false
        val model = LoginViewModel(object : AuthRepository {
            override suspend fun signIn(email: String, password: String): StaffUser {
                called = true
                error("Should not be called")
            }
        })
        model.updateEmail("not-an-email")
        model.signIn()
        advanceUntilIdle()
        assertTrue(model.state.value.invalidEmail)
        assertTrue(model.state.value.missingPassword)
        assertFalse(called)
    }

    @Test fun `successful login clears password and sign out clears session`() = runTest(dispatcher) {
        val model = LoginViewModel(DemoAuthRepository())
        model.useDemoAccount()
        model.signIn()
        assertTrue(model.state.value.isLoading)
        advanceUntilIdle()
        assertEquals(DemoAuthRepository.EMAIL, model.state.value.user?.email)
        assertEquals("", model.state.value.password)
        model.signOut()
        assertEquals(LoginUiState(), model.state.value)
    }

    @Test fun `wrong password shows an error without creating a session`() = runTest(dispatcher) {
        val model = LoginViewModel(DemoAuthRepository())
        model.updateEmail(DemoAuthRepository.EMAIL)
        model.updatePassword("wrong-password")
        model.signIn()
        advanceUntilIdle()
        assertNull(model.state.value.user)
        assertEquals(LoginError.InvalidCredentials, model.state.value.error)
        assertFalse(model.state.value.isLoading)
    }

    @Test fun `email is trimmed but password is preserved and repeated submit is ignored`() = runTest(dispatcher) {
        var calls = 0
        val model = LoginViewModel(object : AuthRepository {
            override suspend fun signIn(email: String, password: String): StaffUser {
                calls++
                delay(100)
                assertEquals("equipo@example.com", email)
                assertEquals(" password ", password)
                return StaffUser("Equipo", email)
            }
        })
        model.updateEmail(" equipo@example.com ")
        model.updatePassword(" password ")
        model.signIn()
        model.signIn()
        advanceUntilIdle()
        assertEquals(1, calls)
    }

    @Test fun `unexpected repository failure allows retry`() = runTest(dispatcher) {
        val model = LoginViewModel(object : AuthRepository {
            override suspend fun signIn(email: String, password: String): StaffUser = error("Unavailable")
        })
        model.useDemoAccount()
        model.signIn()
        advanceUntilIdle()
        assertEquals(LoginError.Unavailable, model.state.value.error)
        assertFalse(model.state.value.isLoading)
        model.updatePassword("retry")
        assertNull(model.state.value.error)
    }
}
