package com.dulcesabor.android.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val colors = lightColorScheme(
    primary = Color(0xFF557450),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFEAF0E5),
    onPrimaryContainer = Color(0xFF354E32),
    background = Color(0xFFFAF8F5),
    onBackground = Color(0xFF242724),
    surface = Color(0xFFFAF8F5),
    onSurface = Color(0xFF242724),
    surfaceVariant = Color(0xFFF2F0EC),
    onSurfaceVariant = Color(0xFF62645D),
    outline = Color(0xFFBFC1B7),
    error = Color(0xFFB3261E),
)

private val typography = Typography(
    headlineLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 34.sp,
        lineHeight = 40.sp,
        letterSpacing = (-0.7).sp,
    ),
    bodyLarge = TextStyle(fontSize = 16.sp, lineHeight = 24.sp),
    bodyMedium = TextStyle(fontSize = 14.sp, lineHeight = 20.sp),
    labelLarge = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 16.sp, lineHeight = 22.sp),
)

@Composable
fun DulceSaborTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = colors,
        typography = typography,
        shapes = Shapes(small = RoundedCornerShape(10.dp), medium = RoundedCornerShape(12.dp)),
        content = content,
    )
}
