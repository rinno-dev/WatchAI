package com.example.watchai

import android.app.Application
import com.example.watchai.data.local.AppDatabase
import com.example.watchai.data.remote.GeminiApiService
import com.example.watchai.data.repository.ChatRepository

class WatchAiApplication : Application() {

    val database by lazy { AppDatabase.getDatabase(this) }
    val apiService by lazy { GeminiApiService() }
    val chatRepository by lazy {
        ChatRepository(chatDao = database.chatDao(), apiService = apiService)
    }
}
