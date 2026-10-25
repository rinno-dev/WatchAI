package com.example.watchai.data.remote

import com.example.watchai.BuildConfig

object GeminiConfig {
    const val API_KEY: String = BuildConfig.GEMINI_API_KEY
    const val BASE_URL: String = "https://generativelanguage.googleapis.com/v1beta/models"
    const val MODEL: String = "gemini-3.5-flash-lite"
    const val SYSTEM_PROMPT: String =
        "Ты AI-ассистент на умных часах Wear OS. Отвечай кратко, чётко и информативно, " +
            "чтобы ответ удобно читался на маленьком круглом экране."
}
