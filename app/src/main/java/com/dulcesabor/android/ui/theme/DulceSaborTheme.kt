package com.dulcesabor.android.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/** Colores del taller: papel de horno, crema batida, chocolate y los acentos del logo. */
object Pasteleria {
    val PapelHorno = Color(0xFFFFFFFF)
    val CremaBatida = Color(0xFFFFFCFA)
    val Masa = Color(0xFFF8F3EF)
    val Chocolate = Color(0xFF3A2620)
    val Cacao = Color(0xFF7A6259)
    val Harina = Color(0xFFA8948A)
    val Frambuesa = Color(0xFFF4C6D7)
    val FrambuesaSuave = Color(0xFFFCEEF3)
    val Celeste = Color(0xFF4F8DB0)
    val Hilo = Color(0x143A2620)
    val Error = Color(0xFFB3261E)
}

private val colors = lightColorScheme(
    primary = Pasteleria.Frambuesa,
    onPrimary = Pasteleria.Chocolate,
    primaryContainer = Pasteleria.FrambuesaSuave,
    onPrimaryContainer = Pasteleria.Chocolate,
    secondary = Pasteleria.Celeste,
    onSecondary = Color.White,
    background = Pasteleria.PapelHorno,
    onBackground = Pasteleria.Chocolate,
    surface = Pasteleria.CremaBatida,
    onSurface = Pasteleria.Chocolate,
    surfaceVariant = Pasteleria.Masa,
    onSurfaceVariant = Pasteleria.Cacao,
    outline = Pasteleria.Hilo,
    outlineVariant = Pasteleria.Hilo,
    error = Pasteleria.Error,
)

private val typography = Typography(
    headlineLarge = TextStyle(
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.Bold,
        fontSize = 36.sp,
        lineHeight = 40.sp,
        letterSpacing = (-0.8).sp,
    ),
    bodyLarge = TextStyle(fontSize = 16.sp, lineHeight = 24.sp),
    bodyMedium = TextStyle(fontSize = 14.sp, lineHeight = 20.sp),
    labelLarge = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 16.sp, lineHeight = 22.sp),
    labelSmall = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 11.sp, lineHeight = 14.sp, letterSpacing = 1.4.sp),
)

@Composable
fun DulceSaborTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = colors,
        typography = typography,
        shapes = Shapes(
            small = RoundedCornerShape(8.dp),
            medium = RoundedCornerShape(12.dp),
            large = RoundedCornerShape(16.dp),
        ),
        content = content,
    )
}
