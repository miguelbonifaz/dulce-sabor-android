package com.dulcesabor.android.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
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
import com.dulcesabor.android.R
import com.dulcesabor.android.auth.LoginError
import com.dulcesabor.android.auth.LoginUiState
import com.dulcesabor.android.ui.theme.DulceSaborTheme
import com.dulcesabor.android.ui.theme.Pasteleria

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
                LogoLabel()
                Spacer(Modifier.height(24.dp))
                Text(
                    stringResource(R.string.login_title),
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    stringResource(R.string.login_subtitle),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(Modifier.height(24.dp))

                // La comanda: el ticket de pedido que sostiene el formulario.
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(elevation = 6.dp, shape = ComandaShape, ambientColor = Pasteleria.Chocolate, spotColor = Pasteleria.Chocolate)
                        .background(MaterialTheme.colorScheme.surface, ComandaShape)
                        .padding(start = 20.dp, end = 20.dp, top = 28.dp, bottom = 20.dp),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(
                            stringResource(R.string.order_label).uppercase(),
                            style = MaterialTheme.typography.labelSmall,
                            color = Pasteleria.Harina,
                        )
                        DemoStamp()
                    }
                    Spacer(Modifier.height(12.dp))
                    DashedRule()
                    Spacer(Modifier.height(20.dp))

                    val fieldColors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        unfocusedBorderColor = Color.Transparent,
                        disabledBorderColor = Color.Transparent,
                        focusedBorderColor = MaterialTheme.colorScheme.secondary,
                        focusedLabelColor = MaterialTheme.colorScheme.secondary,
                        cursorColor = MaterialTheme.colorScheme.secondary,
                        unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    OutlinedTextField(
                        value = state.email,
                        onValueChange = onEmailChange,
                        label = { Text(stringResource(R.string.email_label)) },
                        placeholder = { Text(stringResource(R.string.email_placeholder), color = Pasteleria.Harina) },
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
                    Spacer(Modifier.height(12.dp))
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
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
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
                        Text(
                            stringResource(R.string.forgot_password),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.secondary,
                        )
                    }
                    state.error?.let { error ->
                        Text(
                            stringResource(if (error == LoginError.InvalidCredentials) R.string.invalid_credentials else R.string.login_unavailable),
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(bottom = 12.dp).semantics { liveRegion = LiveRegionMode.Polite },
                        )
                    }
                    Spacer(Modifier.height(4.dp))
                    Button(
                        onClick = submit,
                        enabled = !state.isLoading,
                        modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp),
                        shape = MaterialTheme.shapes.medium,
                    ) {
                        if (state.isLoading) {
                            CircularProgressIndicator(Modifier.size(20.dp), strokeWidth = 2.dp, color = MaterialTheme.colorScheme.onPrimary)
                            Spacer(Modifier.size(12.dp))
                        }
                        Text(stringResource(if (state.isLoading) R.string.signing_in else R.string.sign_in))
                    }
                }

                Spacer(Modifier.height(16.dp))
                TextButton(
                    onClick = onDemoAccount,
                    enabled = !state.isLoading,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                ) {
                    Text(
                        stringResource(R.string.demo_account),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(Icons.Outlined.Lock, contentDescription = null, modifier = Modifier.size(14.dp), tint = Pasteleria.Harina)
                    Text(stringResource(R.string.staff_only), style = MaterialTheme.typography.bodyMedium, color = Pasteleria.Harina)
                }
            }
            Text(
                stringResource(R.string.brand_caption),
                modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 32.dp),
                style = MaterialTheme.typography.bodyMedium.copy(fontStyle = FontStyle.Italic, fontFamily = FontFamily.Serif),
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

/** Ticket con borde dentado arriba, como una comanda arrancada del talonario. */
private val ComandaShape = GenericShape { size, _ ->
    val tooth = 16f
    val depth = 7f
    val radius = 40f
    val teeth = (size.width / tooth).toInt().coerceAtLeast(1)
    val step = size.width / teeth
    moveTo(0f, depth)
    for (i in 0 until teeth) {
        lineTo(i * step + step / 2, 0f)
        lineTo((i + 1) * step, depth)
    }
    lineTo(size.width, size.height - radius)
    quadraticTo(size.width, size.height, size.width - radius, size.height)
    lineTo(radius, size.height)
    quadraticTo(0f, size.height, 0f, size.height - radius)
    close()
}

@Composable
private fun LogoLabel() {
    Box(
        modifier = Modifier
            .size(88.dp)
            .shadow(3.dp, CircleShape, ambientColor = Pasteleria.Chocolate, spotColor = Pasteleria.Chocolate)
            .background(Color.White, CircleShape)
            .padding(10.dp),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(R.drawable.dulce_sabor_logo),
            contentDescription = stringResource(R.string.app_name),
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Composable
private fun DemoStamp() {
    Text(
        stringResource(R.string.demo_mode).uppercase(),
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier
            .rotate(-4f)
            .border(1.5.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.6f), RoundedCornerShape(4.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp),
    )
}

@Composable
private fun DashedRule() {
    val color = Pasteleria.Hilo
    Canvas(Modifier.fillMaxWidth().height(1.dp)) {
        drawLine(
            color = color,
            start = Offset(0f, 0f),
            end = Offset(size.width, 0f),
            strokeWidth = 1.dp.toPx() * 1.5f,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 8f)),
        )
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun LoginPreview() {
    DulceSaborTheme { LoginScreen(LoginUiState(), {}, {}, {}, {}) }
}
