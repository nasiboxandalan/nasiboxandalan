package com.example.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val AgendaLightColorScheme = lightColorScheme(
    primary = ForestGreen,
    onPrimary = Color.White,
    primaryContainer = ForestGreenSoft,
    onPrimaryContainer = ForestGreenDark,
    secondary = TurmericAmber,
    onSecondary = InkDark,
    secondaryContainer = TurmericSoft,
    onSecondaryContainer = TurmericAmberDark,
    tertiary = ForestGreenLight,
    onTertiary = Color.White,
    background = WarmCream,
    onBackground = InkDark,
    surface = CardWhite,
    onSurface = InkDark,
    surfaceVariant = WarmCreamSurface,
    onSurfaceVariant = InkMuted,
    outline = WarmCreamBorder,
    outlineVariant = Color(0xFFEDE6D6),
    error = ErrorRed,
    onError = Color.White,
    errorContainer = ErrorRedSoft,
    onErrorContainer = ErrorRed
)

val AgendaShapes = Shapes(
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(22.dp),
    extraLarge = RoundedCornerShape(28.dp)
)

@Composable
fun AgendaKlatenTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = AgendaLightColorScheme,
        typography = AgendaTypography,
        shapes = AgendaShapes,
        content = content
    )
}
