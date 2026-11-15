package com.dulcesabor.android.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dulcesabor.android.R
import com.dulcesabor.android.auth.LoginError
import com.dulcesabor.android.auth.LoginUiState
import com.dulcesabor.android.ui.theme.DulceSaborTheme

@Composable
fun LoginScreen(
    state: LoginUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onDemoAccount: () -> Unit,
    onSignIn: () -> Unit,
) {
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var showRecovery by rememberSaveable { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val submit = {
        focusManager.clearFocus()
        onSignIn()
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .safeDrawingPadding()
            .imePadding(),
        contentAlignment = Alignment.TopCenter,
    ) {
        val minimumHeight = (maxHeight - 48.dp).coerceAtLeast(0.dp)
        Column(
            modifier = Modifier
                .widthIn(max = 440.dp)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 24.dp)
                .heightIn(min = minimumHeight),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Image(
                        painter = painterResource(R.drawable.dulce_sabor_logo),
                        contentDescription = stringResource(R.string.app_name),
                        modifier = Modifier.size(96.dp),
                    )
                    Surface(
                        shape = MaterialTheme.shapes.small,
                        color = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    ) {
                        Text(
                            text = stringResource(R.string.demo_mode),
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            fontSize = 12.sp,
                        )
                    }
                }
                Spacer(Modifier.height(28.dp))
                Text(stringResource(R.string.login_title), style = MaterialTheme.typography.headlineLarge)
                Spacer(Modifier.height(12.dp))
                Text(
                    stringResource(R.string.login_subtitle),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(Modifier.height(28.dp))

                val fieldColors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                )
                OutlinedTextField(
                    value = state.email,
                    onValueChange = onEmailChange,
                    label = { Text(stringResource(R.string.email_label)) },
                    placeholder = { Text(stringResource(R.string.email_placeholder)) },
                    singleLine = true,
                    enabled = !state.isLoading,
                    isError = state.invalidEmail,
                    supportingText = if (state.invalidEmail) {
                        { Text(stringResource(R.string.invalid_email)) }
                    } else null,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                    shape = MaterialTheme.shapes.medium,
                    colors = fieldColors,
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(Modifier.height(16.dp))
                OutlinedTextField(
                    value = state.password,
                    onValueChange = onPasswordChange,
                    label = { Text(stringResource(R.string.password_label)) },
                    singleLine = true,
                    enabled = !state.isLoading,
                    isError = state.missingPassword,
                    supportingText = if (state.missingPassword) {
                        { Text(stringResource(R.string.required_password)) }
                    } else null,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { if (!state.isLoading) submit() }),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                painter = painterResource(if (passwordVisible) R.drawable.visibility_off else R.drawable.visibility),
                                contentDescription = stringResource(if (passwordVisible) R.string.hide_password else R.string.show_password),
                            )
                        }
                    },
                    shape = MaterialTheme.shapes.medium,
                    colors = fieldColors,
                    modifier = Modifier.fillMaxWidth(),
                )
                TextButton(
                    onClick = { showRecovery = true },
                    modifier = Modifier.align(Alignment.End),
                    enabled = !state.isLoading,
                ) {
                    Text(stringResource(R.string.forgot_password), style = MaterialTheme.typography.bodyMedium)
                }
                state.error?.let { error ->
                    Text(
                        stringResource(if (error == LoginError.InvalidCredentials) R.string.invalid_credentials else R.string.login_unavailable),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 12.dp).semantics { liveRegion = LiveRegionMode.Polite },
                    )
                }
                Spacer(Modifier.height(12.dp))
                Button(
                    onClick = submit,
                    enabled = !state.isLoading,
                    modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp),
                    shape = MaterialTheme.shapes.medium,
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator(Modifier.size(20.dp), strokeWidth = 2.dp)
                        Spacer(Modifier.size(12.dp))
                    }
                    Text(stringResource(if (state.isLoading) R.string.signing_in else R.string.sign_in))
                }
                TextButton(
                    onClick = onDemoAccount,
                    enabled = !state.isLoading,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                ) {
                    Text(stringResource(R.string.demo_account), style = MaterialTheme.typography.bodyMedium)
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(Icons.Outlined.Lock, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(stringResource(R.string.staff_only), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            Text(
                stringResource(R.string.brand_caption),
                modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 32.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }

    if (showRecovery) {
        AlertDialog(
            onDismissRequest = { showRecovery = false },
            title = { Text(stringResource(R.string.recovery_title)) },
            text = { Text(stringResource(R.string.recovery_unavailable)) },
            confirmButton = {
                TextButton(onClick = { showRecovery = false }) { Text(stringResource(R.string.understood)) }
            },
        )
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun LoginPreview() {
    DulceSaborTheme { LoginScreen(LoginUiState(), {}, {}, {}, {}) }
}
