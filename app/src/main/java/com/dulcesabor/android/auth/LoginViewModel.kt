package com.dulcesabor.android.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dulcesabor.android.data.DemoAuthRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class LoginError { InvalidCredentials, Unavailable }

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val invalidEmail: Boolean = false,
    val missingPassword: Boolean = false,
    val isLoading: Boolean = false,
    val error: LoginError? = null,
    val user: StaffUser? = null,
)

class LoginViewModel(private val repository: AuthRepository) : ViewModel() {
    private val mutableState = MutableStateFlow(LoginUiState())
    val state = mutableState.asStateFlow()

    fun updateEmail(value: String) {
        if (!state.value.isLoading) {
            mutableState.update { it.copy(email = value, invalidEmail = false, error = null) }
        }
    }

    fun updatePassword(value: String) {
        if (!state.value.isLoading) {
            mutableState.update { it.copy(password = value, missingPassword = false, error = null) }
        }
    }

    fun useDemoAccount() {
        if (!state.value.isLoading) {
            mutableState.value = LoginUiState(
                email = DemoAuthRepository.EMAIL,
                password = DemoAuthRepository.PASSWORD,
            )
        }
    }

    fun signIn() {
        val current = state.value
        if (current.isLoading || current.user != null) return
        val invalidEmail = !LoginValidation.isValidEmail(current.email)
        val missingPassword = current.password.isBlank()
        mutableState.update {
            it.copy(invalidEmail = invalidEmail, missingPassword = missingPassword, error = null)
        }
        if (invalidEmail || missingPassword) return

        mutableState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                val user = repository.signIn(current.email.trim(), current.password)
                mutableState.value = LoginUiState(email = user.email, user = user)
            } catch (exception: CancellationException) {
                throw exception
            } catch (_: InvalidCredentialsException) {
                mutableState.update { it.copy(isLoading = false, error = LoginError.InvalidCredentials) }
            } catch (_: Exception) {
                mutableState.update { it.copy(isLoading = false, error = LoginError.Unavailable) }
            }
        }
    }

    fun signOut() {
        mutableState.value = LoginUiState()
    }
}
