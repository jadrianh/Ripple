package org.ripple.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme

val RippleBlue = Color(0xFF00AAFF)
val RippleCyan = Color(0xFF95F4FF)
val RippleAccent = Color(0xFF00A7F8)
val RippleDark = Color(0xFF28282B)
val RippleGray = Color(0xFFC0C0C0)
val RippleWhite = Color(0xFFFFFFFF)

val RippleLightColorScheme = lightColorScheme(
    primary = RippleAccent,
    onPrimary = RippleWhite,
    primaryContainer = RippleBlue,
    onPrimaryContainer = RippleWhite,

    secondary = RippleCyan,
    onSecondary = RippleDark,
    secondaryContainer = RippleCyan,
    onSecondaryContainer = RippleDark,

    background = RippleWhite,
    onBackground = RippleDark,
    surface = RippleWhite,
    onSurface = RippleDark,

    surfaceVariant = RippleWhite,
    onSurfaceVariant = RippleDark,

    outline = RippleGray,
    outlineVariant = RippleGray,

    error = Color(0xFFB3261E),
    onError = RippleWhite,
    errorContainer = Color(0xFFF9DEDC),
    onErrorContainer = Color(0xFF410E0B)
)

val RippleDarkColorScheme = darkColorScheme(
    primary = RippleAccent,
    onPrimary = RippleDark,
    primaryContainer = RippleBlue,
    onPrimaryContainer = RippleWhite,

    secondary = RippleCyan,
    onSecondary = RippleDark,
    secondaryContainer = RippleAccent,
    onSecondaryContainer = RippleWhite,

    background = RippleDark,
    onBackground = RippleWhite,
    surface = Color(0xFF1D1D20),
    onSurface = RippleWhite,

    surfaceVariant = Color(0xFF2A2A2E),
    onSurfaceVariant = RippleWhite,

    outline = RippleGray,
    outlineVariant = Color(0xFF6A6A72),

    error = Color(0xFFF2B8B5),
    onError = Color(0xFF601410),
    errorContainer = Color(0xFF8C1D18),
    onErrorContainer = Color(0xFFF9DEDC)
)

