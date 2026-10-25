package com.example.watchai.ui.screens.chatlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.watchai.data.repository.ChatRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ChatListViewModel(
    private val repository: ChatRepository
) : ViewModel() {

    val uiState: StateFlow<ChatListUiState> = repository.allSessions
        .map { sessions ->
            ChatListUiState(
                chats = sessions,
                isLoading = false
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ChatListUiState(isLoading = true)
        )

    fun createNewChat(onCreated: (Long) -> Unit) {
        viewModelScope.launch {
            val newChatId = repository.createNewChat()
            onCreated(newChatId)
        }
    }

    fun deleteChat(chatId: Long) {
        viewModelScope.launch {
            repository.deleteChat(chatId)
        }
    }
}
