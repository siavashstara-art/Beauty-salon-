package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection

private val DarkColorScheme = darkColorScheme(
    primary = RoseGoldDarkPrimary,
    onPrimary = RoseGoldDarkOnPrimary,
    primaryContainer = Color(0xFF7A233F),
    onPrimaryContainer = Color(0xFFFFD9E0),
    secondary = Color(0xFFE5BDC3),
    onSecondary = Color(0xFF43292E),
    secondaryContainer = Color(0xFF5B3F44),
    onSecondaryContainer = Color(0xFFFFD9DF),
    tertiary = GoldDarkTertiary,
    onTertiary = Color(0xFF4C2700),
    background = PlumDark,
    onBackground = Color(0xFFF0DEE0),
    surface = SurfaceDark,
    onSurface = Color(0xFFF0DEE0),
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = Color(0xFFD6C2C5)
)

private val LightColorScheme = lightColorScheme(
    primary = RoseGoldPrimary,
    onPrimary = RoseGoldOnPrimary,
    primaryContainer = RoseGoldPrimaryContainer,
    onPrimaryContainer = RoseGoldOnPrimaryContainer,
    secondary = PlumSecondary,
    onSecondary = PlumOnSecondary,
    secondaryContainer = PlumSecondaryContainer,
    onSecondaryContainer = PlumOnSecondaryContainer,
    tertiary = GoldTertiary,
    onTertiary = GoldOnTertiary,
    background = BackgroundLight,
    onBackground = OnBackgroundLight,
    surface = SurfaceLight,
    onSurface = OnSurfaceLight,
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = OnSurfaceVariantLight
)

@Composable
fun TavanaBeautyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep signature beauty salon brand styling
    isRtl: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val layoutDir = if (isRtl) LayoutDirection.Rtl else LayoutDirection.Ltr

    CompositionLocalProvider(LocalLayoutDirection provides layoutDir) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
