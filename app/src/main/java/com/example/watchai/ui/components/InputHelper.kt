package com.example.watchai.ui.components

import android.content.Intent
import android.speech.RecognizerIntent
import androidx.activity.result.ActivityResultLauncher


enum class InputLanguage(
    val code: String,
    val label: String,
    val flag: String
) {
    RUSSIAN("ru-RU", "RU", "🇷🇺"),
    ENGLISH("en-US", "EN", "🇺🇸"),
    UKRAINIAN("uk-UA", "UK", "🇺🇦");

    
    fun next(): InputLanguage = when (this) {
        RUSSIAN -> UKRAINIAN
        UKRAINIAN -> ENGLISH
        ENGLISH -> RUSSIAN
    }
}

object InputHelper {

    
    fun launchVoiceInput(
        launcher: ActivityResultLauncher<Intent>,
        language: InputLanguage = InputLanguage.RUSSIAN,
        promptHint: String = "Говорите..."
    ) {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, language.code)
            putExtra(RecognizerIntent.EXTRA_PROMPT, promptHint)
        }
        launcher.launch(intent)
    }

    
    fun launchTextInput(
        launcher: ActivityResultLauncher<Intent>,
        language: InputLanguage = InputLanguage.RUSSIAN,
        promptHint: String = "Введите текст..."
    ) {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, language.code)
            putExtra(RecognizerIntent.EXTRA_PROMPT, promptHint)
        }
        launcher.launch(intent)
    }

    
    fun extractTextFromResult(intent: Intent?): String? {
        if (intent == null) return null
        val voiceMatches = intent.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
        val text = voiceMatches?.firstOrNull()
        if (!text.isNullOrBlank()) return text.trim()
        return null
    }
}
