package com.example.watchai.ui.screens.chatlist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Card
import androidx.wear.compose.material.CardDefaults
import androidx.wear.compose.material.CircularProgressIndicator
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.ScalingLazyColumn
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.items
import androidx.wear.compose.material.rememberScalingLazyListState
import com.example.watchai.R
import com.example.watchai.data.local.entity.ChatSessionEntity
import com.example.watchai.ui.components.attachRotaryScroll
import com.example.watchai.ui.theme.Black
import com.example.watchai.ui.theme.DeepSeekCyan
import com.example.watchai.ui.theme.SurfaceCard
import com.example.watchai.ui.theme.TextPrimary
import com.example.watchai.ui.theme.TextSecondary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ChatListScreen(
    viewModel: ChatListViewModel,
    onNavigateToChat: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val listState = rememberScalingLazyListState()

    Scaffold(
        timeText = { TimeText() },
        positionIndicator = { PositionIndicator(scalingLazyListState = listState) },
        modifier = modifier.background(Black)
    ) {
        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    indicatorColor = DeepSeekCyan,
                    modifier = Modifier.size(32.dp)
                )
            }
        } else {
            ScalingLazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .attachRotaryScroll(listState),
                state = listState,
                verticalArrangement = Arrangement.spacedBy(6.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item(key = "header_title") {
                    Text(
                        text = stringResource(R.string.gemini_ai),
                        style = MaterialTheme.typography.title3,
                        fontWeight = FontWeight.Bold,
                        color = DeepSeekCyan,
                        modifier = Modifier.padding(top = 16.dp, bottom = 4.dp)
                    )
                }

                item(key = "btn_new_chat") {
                    Button(
                        onClick = {
                            viewModel.createNewChat { newId ->
                                onNavigateToChat(newId)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .height(44.dp),
                        colors = ButtonDefaults.primaryButtonColors(
                            backgroundColor = DeepSeekCyan,
                            contentColor = Black
                        )
                    ) {
                        Text(
                            text = stringResource(R.string.new_chat),
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }

                if (uiState.chats.isEmpty()) {
                    item(key = "empty_state") {
                        Text(
                            text = stringResource(R.string.history_empty),
                            style = MaterialTheme.typography.caption2,
                            color = TextSecondary,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                        )
                    }
                } else {
                    items(
                        items = uiState.chats,
                        key = { chat -> chat.id }
                    ) { chat ->
                        ChatItem(
                            chat = chat,
                            onClick = { onNavigateToChat(chat.id) },
                            onDelete = { viewModel.deleteChat(chat.id) }
                        )
                    }
                }

                item(key = "bottom_spacer") {
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}

@Composable
private fun ChatItem(
    chat: ChatSessionEntity,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(0.92f),
        backgroundPainter = CardDefaults.cardBackgroundPainter(
            startBackgroundColor = SurfaceCard,
            endBackgroundColor = SurfaceCard
        ),
        contentColor = TextPrimary
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = chat.title,
                    style = MaterialTheme.typography.body2,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = TextPrimary
                )
                Text(
                    text = formatTimestamp(chat.updatedAt),
                    style = MaterialTheme.typography.caption3,
                    color = TextSecondary
                )
            }

            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF262626))
                    .padding(4.dp),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = onDelete,
                    modifier = Modifier.size(24.dp),
                    colors = ButtonDefaults.secondaryButtonColors(
                        backgroundColor = Color.Transparent,
                        contentColor = Color(0xFFEF4444)
                    )
                ) {
                    Text(
                        text = stringResource(R.string.delete_symbol),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFEF4444)
                    )
                }
            }
        }
    }
}

private fun formatTimestamp(timestamp: Long): String {
    val date = Date(timestamp)
    val formatter = SimpleDateFormat("HH:mm, dd MMM", Locale.getDefault())
    return formatter.format(date)
}
