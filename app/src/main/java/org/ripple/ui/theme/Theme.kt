package org.ripple.ui.theme

import android.content.Context
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

internal val Context.rippleDataStore by preferencesDataStore(name = "ripple_prefs")

private object RipplePrefsKeys {
    val DarkThemeEnabled: Preferences.Key<Boolean> = booleanPreferencesKey("dark_theme_enabled")
}

/**
 * Exposed so Settings can persist the theme preference.
 */
object RippleThemePreferences {
    fun darkThemeEnabledFlow(context: Context): Flow<Boolean?> =
        context.rippleDataStore.data
            .catch { e ->
                if (e is IOException) emit(emptyPreferences()) else throw e
            }
            .map { prefs -> prefs[RipplePrefsKeys.DarkThemeEnabled] }

    suspend fun setDarkThemeEnabled(context: Context, enabled: Boolean) {
        context.rippleDataStore.edit { prefs ->
            prefs[RipplePrefsKeys.DarkThemeEnabled] = enabled
        }
    }
}

val RippleGradientBrush: Brush = Brush.verticalGradient(
    colors = listOf(RippleBlue, RippleCyan)
)

@Composable
fun RippleTheme(
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val systemDark = isSystemInDarkTheme()

    val storedPref by RippleThemePreferences
        .darkThemeEnabledFlow(context)
        .collectAsState(initial = null)

    val useDarkTheme = storedPref ?: systemDark

    MaterialTheme(
        colorScheme = if (useDarkTheme) RippleDarkColorScheme else RippleLightColorScheme,
        typography = Typography,
        content = content
    )
}
