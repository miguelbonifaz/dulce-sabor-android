package com.dulcesabor.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.dulcesabor.android.auth.LoginViewModel
import com.dulcesabor.android.data.DemoAuthRepository
import com.dulcesabor.android.ui.LoginScreen
import com.dulcesabor.android.ui.DemoSessionScreen
import com.dulcesabor.android.ui.theme.DulceSaborTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DulceSaborTheme {
                val viewModel: LoginViewModel = viewModel(factory = viewModelFactory {
                    initializer { LoginViewModel(DemoAuthRepository()) }
                })
                val state by viewModel.state.collectAsStateWithLifecycle()
                val user = state.user
                if (user == null) {
                    LoginScreen(
                        state = state,
                        onEmailChange = viewModel::updateEmail,
                        onPasswordChange = viewModel::updatePassword,
                        onDemoAccount = viewModel::useDemoAccount,
                        onSignIn = viewModel::signIn,
                    )
                } else {
                    DemoSessionScreen(user = user, onSignOut = viewModel::signOut)
                }
            }
        }
    }
}
