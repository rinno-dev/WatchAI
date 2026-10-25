package com.example.watchai.data.repository

import com.example.watchai.data.local.dao.ChatDao
import com.example.watchai.data.local.entity.ChatMessageEntity
import com.example.watchai.data.local.entity.ChatSessionEntity
import com.example.watchai.data.remote.GeminiApiService
import com.example.watchai.data.remote.GeminiConfig
import com.example.watchai.data.remote.GeminiMessageDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class ChatRepository(
    private val chatDao: ChatDao,
    private val apiService: GeminiApiService
) {

    val allSessions: Flow<List<ChatSessionEntity>> = chatDao.getAllSessions()

    fun getMessages(chatId: Long): Flow<List<ChatMessageEntity>> =
        chatDao.getMessagesForChat(chatId)

    suspend fun createNewChat(initialTitle: String = "Новый чат"): Long = withContext(Dispatchers.IO) {
        chatDao.insertSession(
            ChatSessionEntity(
                title = initialTitle,
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )
        )
    }

    suspend fun deleteChat(chatId: Long) = withContext(Dispatchers.IO) {
        chatDao.deleteSessionById(chatId)
    }

    suspend fun sendMessage(chatId: Long, userText: String): Result<String> = withContext(Dispatchers.IO) {
        val cleanText = userText.trim()
        if (cleanText.isEmpty()) {
            return@withContext Result.failure(IllegalArgumentException("Пустой текст"))
        }

        chatDao.insertMessage(
            ChatMessageEntity(chatId = chatId, role = "user", content = cleanText, timestamp = System.currentTimeMillis())
        )

        val currentSession = chatDao.getSessionById(chatId)
        if (currentSession != null) {
            val updatedTitle = if (currentSession.title == "Новый чат" || currentSession.title.isBlank()) {
                val snippet = cleanText.take(22).trim()
                if (cleanText.length > 22) "$snippet…" else snippet
            } else {
                currentSession.title
            }
            chatDao.updateSession(currentSession.copy(title = updatedTitle, updatedAt = System.currentTimeMillis()))
        }

        val history = chatDao.getMessagesForChatList(chatId)
        val dtoList = mutableListOf(
            GeminiMessageDto(role = "system", content = GeminiConfig.SYSTEM_PROMPT)
        )
        for (item in history.takeLast(8)) {
            if (!item.isError) {
                dtoList.add(GeminiMessageDto(role = item.role, content = item.content))
            }
        }

        val result = apiService.sendChatCompletion(dtoList)

        if (result.isSuccess) {
            val responseText = result.getOrThrow()
            chatDao.insertMessage(
                ChatMessageEntity(chatId = chatId, role = "assistant", content = responseText, timestamp = System.currentTimeMillis(), isError = false)
            )
            Result.success(responseText)
        } else {
            val error = result.exceptionOrNull()
            val errorMessage = error?.localizedMessage ?: "Сетевая ошибка"
            chatDao.insertMessage(
                ChatMessageEntity(chatId = chatId, role = "assistant", content = "⚠️ $errorMessage", timestamp = System.currentTimeMillis(), isError = true)
            )
            Result.failure(error ?: Exception(errorMessage))
        }
    }
}
