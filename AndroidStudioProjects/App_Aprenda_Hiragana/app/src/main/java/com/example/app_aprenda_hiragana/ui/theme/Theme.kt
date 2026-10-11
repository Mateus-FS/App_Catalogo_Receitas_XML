package com.example.app_aprenda_hiragana.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Indigo80,
    onPrimary = Color(0xFF16226E),
    primaryContainer = IndigoContainer30,
    onPrimaryContainer = OnIndigoContainer90,
    secondary = Sakura80,
    background = Noite10,
    onBackground = Texto90,
    surface = Noite10,
    onSurface = Texto90,
    surfaceVariant = Noite20,
    onSurfaceVariant = Texto70,
    outline = ContornoEscuro
)

private val LightColorScheme = lightColorScheme(
    primary = Indigo40,
    onPrimary = Color.White,
    primaryContainer = IndigoContainer90,
    onPrimaryContainer = OnIndigoContainer10,
    secondary = Sakura40,
    background = Papel98,
    onBackground = Tinta10,
    surface = Papel98,
    onSurface = Tinta10,
    surfaceVariant = Branco,
    onSurfaceVariant = Tinta40,
    outline = Contorno80
)

@Composable
fun App_Aprenda_HiraganaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
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

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}