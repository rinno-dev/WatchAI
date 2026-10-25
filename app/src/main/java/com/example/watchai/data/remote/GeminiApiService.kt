package com.example.watchai.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class GeminiApiService(
    private val client: HttpClient = createDefaultKtorClient()
) {
    suspend fun sendChatCompletion(messages: List<GeminiMessageDto>): Result<String> =
        withContext(Dispatchers.IO) {
            try {
                val apiKey = GeminiConfig.API_KEY
                if (apiKey.isBlank() || apiKey == "YOUR_GEMINI_API_KEY_HERE") {
                    return@withContext Result.failure(
                        IllegalStateException("Добавь GEMINI_API_KEY в local.properties")
                    )
                }

                val systemPrompt = messages.firstOrNull { it.role == "system" }?.content
                val contents = messages
                    .filterNot { it.role == "system" }
                    .map { message ->
                        GeminiContent(
                            role = if (message.role == "assistant" || message.role == "model") "model" else "user",
                            parts = listOf(GeminiPart(text = message.content))
                        )
                    }
                if (contents.isEmpty()) {
                    return@withContext Result.failure(IllegalArgumentException("Нет сообщений для отправки"))
                }

                val request = GeminiGenerateContentRequest(
                    systemInstruction = systemPrompt?.let {
                        GeminiSystemInstruction(parts = listOf(GeminiPart(text = it)))
                    },
                    contents = contents,
                    generationConfig = GeminiGenerationConfig()
                )

                val response: HttpResponse = client.post(
                    "${GeminiConfig.BASE_URL}/${GeminiConfig.MODEL}:generateContent"
                ) {
                    contentType(ContentType.Application.Json)
                    header("x-goog-api-key", apiKey)
                    setBody(request)
                }

                if (response.status.isSuccess()) {
                    val body: GeminiGenerateContentResponse = response.body()
                    val answer = body.candidates
                        .firstOrNull()
                        ?.content
                        ?.parts
                        ?.mapNotNull { it.text }
                        ?.joinToString(separator = "")
                        ?.trim()
                    if (!answer.isNullOrBlank()) {
                        Result.success(answer)
                    } else {
                        val blockReason = body.promptFeedback?.blockReason
                        Result.failure(
                            Exception(blockReason?.let { "Gemini отклонил запрос: $it" }
                                ?: "Gemini вернул пустой ответ.")
                        )
                    }
                } else {
                    val status = response.status.value
                    val errorBody = response.bodyAsText()
                    val providerMessage = runCatching {
                        Json.parseToJsonElement(errorBody)
                            .jsonObject["error"]
                            ?.jsonObject
                            ?.get("message")
                            ?.jsonPrimitive
                            ?.content
                    }.getOrNull()
                    val details = when {
                        status == 400 && providerMessage.orEmpty().contains("API key not valid", ignoreCase = true) ->
                            "Ключ Gemini не распознан. Создай новый ключ в Google AI Studio и замени GEMINI_API_KEY в local.properties."
                        status == 400 -> "Gemini не принял запрос (400): ${providerMessage ?: errorBody}"
                        status == 401 || status == 403 ->
                            "Gemini отклонил API-ключ или доступ к API (HTTP $status): ${providerMessage ?: errorBody}"
                        status == 404 -> "Модель Gemini не найдена (404): ${providerMessage ?: errorBody}"
                        status == 429 -> "Лимит бесплатных запросов Gemini исчерпан. Попробуй позже."
                        status in 500..599 -> "Gemini временно недоступен (HTTP $status). Попробуй позже."
                        else -> "Ошибка Gemini API (HTTP $status): ${providerMessage ?: errorBody}"
                    }
                    Result.failure(Exception(details))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    companion object {
        fun createDefaultKtorClient(): HttpClient = HttpClient(Android) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                    encodeDefaults = true
                })
            }
            install(HttpTimeout) {
                requestTimeoutMillis = 60_000
                connectTimeoutMillis = 15_000
                socketTimeoutMillis = 60_000
            }
        }
    }
}
