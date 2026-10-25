package com.example.watchai.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.wear.compose.material.Colors

val Black = Color(0xFF000000)
val DarkBackground = Color(0xFF050505)
val SurfaceCard = Color(0xFF131722)

val DeepSeekCyan = Color(0xFF06B6D4)
val DeepSeekBlue = Color(0xFF0284C7)
val DeepSeekBlueDark = Color(0xFF0369A1)
val DeepSeekLightBlue = Color(0xFF38BDF8)

val UserBubbleColor = Color(0xFF1E293B)
val AssistantBubbleColor = Color(0xFF0F172A)
val ErrorBubbleColor = Color(0xFF3B1212)

val TextPrimary = Color(0xFFF8FAFC)
val TextSecondary = Color(0xFF94A3B8)
val TextError = Color(0xFFFCA5A5)

internal val WearColorPalette: Colors = Colors(
    primary = DeepSeekCyan,
    primaryVariant = DeepSeekBlue,
    secondary = DeepSeekLightBlue,
    background = Black,
    surface = SurfaceCard,
    onPrimary = Black,
    onSecondary = Black,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    onError = TextError
)
