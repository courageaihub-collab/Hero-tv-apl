package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val TvDarkColorScheme = darkColorScheme(
    primary = TvPrimary,
    onPrimary = TvBackground,
    primaryContainer = TvPrimaryDark,
    onPrimaryContainer = TvTextPrimary,
    secondary = TvSecondary,
    onSecondary = TvBackground,
    secondaryContainer = TvSurfaceHighlight,
    onSecondaryContainer = TvTextPrimary,
    tertiary = TvAccentViolet,
    background = TvBackground,
    onBackground = TvTextPrimary,
    surface = TvSurface,
    onSurface = TvTextPrimary,
    surfaceVariant = TvSurfaceVariant,
    onSurfaceVariant = TvTextSecondary,
    outline = TvCardBorder,
    error = TvError,
    onError = TvTextPrimary
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // HERO TV defaults to cinematic deep dark TV theme
    dynamicColor: Boolean = false, // Keep consistent tailored TV violet palette
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = TvDarkColorScheme,
        typography = Typography,
        content = content
    )
}
