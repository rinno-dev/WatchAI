package com.example.watchai.ui.navigation

sealed class Screen(val route: String) {
    data object ChatList : Screen("chat_list")
    data object ChatDetail : Screen("chat_detail/{chatId}") {
        fun createRoute(chatId: Long): String = "chat_detail/$chatId"
    }
}
