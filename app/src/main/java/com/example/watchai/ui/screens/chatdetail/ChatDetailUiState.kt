package com.example.watchai.ui.screens.chatdetail

import androidx.compose.runtime.Immutable
import com.example.watchai.data.local.entity.ChatMessageEntity


@Immutable
data class ChatDetailUiState(
    val messages: List<ChatMessageEntity> = emptyList(),
    val isGenerating: Boolean = false,
    val errorMessage: String? = null
)
