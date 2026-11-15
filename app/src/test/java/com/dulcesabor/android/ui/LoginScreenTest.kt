package com.dulcesabor.android.ui

import android.graphics.Bitmap
import android.graphics.Canvas
import java.io.File
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import com.dulcesabor.android.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35], qualifiers = "w390dp-h844dp-xhdpi")
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class LoginScreenTest {
    @get:Rule val compose = createAndroidComposeRule<MainActivity>()

    @Test fun emptySubmissionShowsFieldErrors() {
        compose.onNodeWithContentDescription("Dulce Sabor").assertIsDisplayed()
        compose.runOnIdle {
            val view = compose.activity.window.decorView
            val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
            view.draw(Canvas(bitmap))
            val target = File("build/outputs/screenshots/login.png")
            target.parentFile?.mkdirs()
            target.outputStream().use { bitmap.compress(Bitmap.CompressFormat.PNG, 100, it) }
            bitmap.recycle()
        }
        compose.onNodeWithText("Ingresar").performScrollTo().performClick()
        compose.onNodeWithText("Ingresa un correo electrónico válido.").performScrollTo().assertIsDisplayed()
        compose.onNodeWithText("Ingresa tu contraseña.").performScrollTo().assertIsDisplayed()
    }

    @Test fun demoLoginAndSignOutWork() {
        compose.onNodeWithText("Usar cuenta de prueba").performScrollTo().performClick()
        compose.onNodeWithContentDescription("Mostrar contraseña").performClick()
        compose.onNodeWithText("DulceSabor2026").assertIsDisplayed()
        compose.onNodeWithText("Ingresar").performScrollTo().performClick()
        compose.waitUntil(timeoutMillis = 10_000) {
            compose.onAllNodes(androidx.compose.ui.test.hasText("Cerrar sesión")).fetchSemanticsNodes().isNotEmpty()
        }
        compose.onNodeWithText("Cerrar sesión").performClick()
        compose.onNodeWithText("Tu taller,\na un toque.").assertIsDisplayed()
    }

    @Test fun recoveryExplainsDemoLimitation() {
        compose.onNodeWithText("¿Olvidaste tu contraseña?").performScrollTo().performClick()
        compose.onNodeWithText("Recuperar contraseña").assertIsDisplayed()
        compose.onNodeWithText("Entendido").performClick()
        compose.onNodeWithText("Ingresar").assertIsDisplayed()
    }

    @Test
    @Config(sdk = [35], qualifiers = "w320dp-h568dp-xhdpi")
    fun compactScreenCanScrollToSignIn() {
        compose.onNodeWithText("Usar cuenta de prueba").performScrollTo().performClick()
        compose.onNodeWithText("Ingresar").performScrollTo().assertIsDisplayed()
    }
}
