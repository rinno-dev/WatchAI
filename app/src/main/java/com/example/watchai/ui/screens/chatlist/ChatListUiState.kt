package com.example.watchai.ui.screens.chatlist

import androidx.compose.runtime.Immutable
import com.example.watchai.data.local.entity.ChatSessionEntity


@Immutable
data class ChatListUiState(
    val chats: List<ChatSessionEntity> = emptyList(),
    val isLoading: Boolean = true
)
