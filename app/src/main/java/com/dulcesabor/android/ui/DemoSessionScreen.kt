package com.dulcesabor.android.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dulcesabor.android.R
import com.dulcesabor.android.auth.StaffUser

@Composable
fun DemoSessionScreen(user: StaffUser, onSignOut: () -> Unit) {
    BackHandler(onBack = onSignOut)
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.safeDrawingPadding().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically),
        ) {
            Image(painterResource(R.drawable.dulce_sabor_logo), stringResource(R.string.app_name), Modifier.size(128.dp))
            Text(stringResource(R.string.welcome), style = MaterialTheme.typography.headlineSmall)
            Text(user.email)
            Text(stringResource(R.string.demo_session_description), style = MaterialTheme.typography.bodyLarge)
            Button(onClick = onSignOut) { Text(stringResource(R.string.sign_out)) }
        }
    }
}
