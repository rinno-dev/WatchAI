package com.example.watchai.ui.screens.chatdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.watchai.data.repository.ChatRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ChatDetailViewModel(
    val chatId: Long,
    private val repository: ChatRepository
) : ViewModel() {

    private val isGenerating = MutableStateFlow(false)
    private val errorMessage = MutableStateFlow<String?>(null)

    val uiState: StateFlow<ChatDetailUiState> = combine(
        repository.getMessages(chatId),
        isGenerating,
        errorMessage
    ) { messages, generating, error ->
        ChatDetailUiState(
            messages = messages,
            isGenerating = generating,
            errorMessage = error
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ChatDetailUiState()
    )

    fun sendMessage(text: String) {
        if (text.isBlank() || isGenerating.value) return

        viewModelScope.launch {
            isGenerating.value = true
            errorMessage.value = null

            val result = repository.sendMessage(chatId, text)
            if (result.isFailure) {
                errorMessage.value = result.exceptionOrNull()?.localizedMessage
            }

            isGenerating.value = false
        }
    }

    fun clearError() {
        errorMessage.value = null
    }
}
