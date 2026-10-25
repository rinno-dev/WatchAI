package com.example.watchai.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.example.watchai.data.repository.ChatRepository
import com.example.watchai.ui.screens.chatdetail.ChatDetailScreen
import com.example.watchai.ui.screens.chatdetail.ChatDetailViewModel
import com.example.watchai.ui.screens.chatlist.ChatListScreen
import com.example.watchai.ui.screens.chatlist.ChatListViewModel

@Composable
fun WatchNavGraph(
    repository: ChatRepository
) {
    val navController = rememberSwipeDismissableNavController()

    SwipeDismissableNavHost(
        navController = navController,
        startDestination = Screen.ChatList.route
    ) {
        composable(route = Screen.ChatList.route) {
            val listViewModel: ChatListViewModel = viewModel {
                ChatListViewModel(repository)
            }

            ChatListScreen(
                viewModel = listViewModel,
                onNavigateToChat = { chatId ->
                    navController.navigate(Screen.ChatDetail.createRoute(chatId))
                }
            )
        }

        composable(
            route = Screen.ChatDetail.route,
            arguments = listOf(
                navArgument("chatId") {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ) { backStackEntry ->
            val chatId = backStackEntry.arguments?.getLong("chatId") ?: -1L
            val detailViewModel: ChatDetailViewModel = remember(chatId) {
                ChatDetailViewModel(chatId = chatId, repository = repository)
            }

            ChatDetailScreen(
                viewModel = detailViewModel
            )
        }
    }
}
