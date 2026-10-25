package com.example.watchai.ui.screens.chatdetail

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.ScalingLazyColumn
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.items
import androidx.wear.compose.material.rememberScalingLazyListState
import com.example.watchai.data.local.entity.ChatMessageEntity
import com.example.watchai.ui.components.InputHelper
import com.example.watchai.ui.components.LoadingDots
import com.example.watchai.ui.components.attachRotaryScroll
import com.example.watchai.ui.theme.AssistantBubbleColor
import com.example.watchai.ui.theme.Black
import com.example.watchai.ui.theme.DeepSeekCyan
import com.example.watchai.ui.theme.ErrorBubbleColor
import com.example.watchai.ui.theme.TextError
import com.example.watchai.ui.theme.TextPrimary
import com.example.watchai.ui.theme.TextSecondary
import com.example.watchai.ui.theme.UserBubbleColor

@Composable
fun ChatDetailScreen(
    viewModel: ChatDetailViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val listState = rememberScalingLazyListState()
    var draft by rememberSaveable { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    val inputLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val text = InputHelper.extractTextFromResult(result.data)
            if (!text.isNullOrBlank()) {
                viewModel.sendMessage(text)
            }
        }
    }

    LaunchedEffect(uiState.messages.size, uiState.isGenerating) {
        if (uiState.messages.isNotEmpty()) {
            val lastItemIndex = uiState.messages.size + if (uiState.isGenerating) 1 else 0
            listState.animateScrollToItem(lastItemIndex)
        }
    }

    Scaffold(
        timeText = { TimeText() },
        positionIndicator = { PositionIndicator(scalingLazyListState = listState) },
        modifier = modifier.background(Black)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ScalingLazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .attachRotaryScroll(listState),
                state = listState,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item(key = "top_spacer") {
                    Spacer(modifier = Modifier.height(18.dp))
                }

                if (uiState.messages.isEmpty() && !uiState.isGenerating) {
                    item(key = "empty_hint") {
                        Text(
                            text = "Введите вопрос или\nнажмите микрофон",
                            style = MaterialTheme.typography.caption2,
                            color = TextSecondary,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
                        )
                    }
                }

                items(
                    items = uiState.messages,
                    key = { message -> message.id }
                ) { message ->
                    MessageBubble(message = message)
                }

                if (uiState.isGenerating) {
                    item(key = "loading_indicator") {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.92f)
                                .clip(RoundedCornerShape(14.dp))
                                .background(AssistantBubbleColor)
                                .border(1.dp, DeepSeekCyan.copy(alpha = 0.4f), RoundedCornerShape(14.dp))
                                .padding(horizontal = 12.dp, vertical = 10.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "Gemini",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DeepSeekCyan
                                )
                                LoadingDots()
                            }
                        }
                    }
                }

                item(key = "bottom_spacer") {
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            MessageComposer(
                draft = draft,
                onDraftChange = { draft = it },
                isGenerating = uiState.isGenerating,
                onVoiceClick = { InputHelper.launchVoiceInput(inputLauncher) },
                onSendClick = {
                    val message = draft.trim()
                    if (message.isNotEmpty() && !uiState.isGenerating) {
                        viewModel.sendMessage(message)
                        draft = ""
                        keyboardController?.hide()
                    }
                },
                modifier = Modifier.padding(bottom = 6.dp)
            )
        }
    }
}

@Composable
private fun MessageComposer(
    draft: String,
    onDraftChange: (String) -> Unit,
    isGenerating: Boolean,
    onVoiceClick: () -> Unit,
    onSendClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val composerShape = RoundedCornerShape(
        topStart = CornerSize(14.dp),
        topEnd = CornerSize(14.dp),
        bottomStart = CornerSize(50),
        bottomEnd = CornerSize(50)
    )

    Column(
        modifier = modifier
            .fillMaxWidth(0.90f)
            .clip(composerShape)
            .background(Color(0xFF18212B))
            .border(1.dp, DeepSeekCyan.copy(alpha = 0.45f), composerShape)
            .padding(6.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 38.dp, max = 76.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0xFF10151C))
                .padding(horizontal = 7.dp, vertical = 5.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            if (draft.isEmpty()) {
                Text(
                    text = "Введите вопрос…",
                    color = TextSecondary,
                    fontSize = 12.sp
                )
            }
            BasicTextField(
                value = draft,
                onValueChange = onDraftChange,
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(color = TextPrimary, fontSize = 13.sp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Send
                ),
                keyboardActions = KeyboardActions(onSend = { onSendClick() }),
                singleLine = false
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = onVoiceClick,
                enabled = !isGenerating,
                modifier = Modifier.size(34.dp),
                colors = ButtonDefaults.secondaryButtonColors(
                    backgroundColor = Color(0xFF242C3D),
                    contentColor = Color.White
                )
            ) {
                Text(text = "🎙️", fontSize = 15.sp)
            }
            Button(
                onClick = onSendClick,
                enabled = !isGenerating && draft.isNotBlank(),
                modifier = Modifier.size(34.dp),
                colors = ButtonDefaults.primaryButtonColors(
                    backgroundColor = DeepSeekCyan,
                    contentColor = Black
                )
            ) {
                Text(text = "➤", fontSize = 15.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun MessageBubble(
    message: ChatMessageEntity,
    modifier: Modifier = Modifier
) {
    val isUser = message.role == "user"
    val isError = message.isError

    val backgroundColor = when {
        isError -> ErrorBubbleColor
        isUser -> UserBubbleColor
        else -> AssistantBubbleColor
    }

    val borderColor = when {
        isError -> TextError.copy(alpha = 0.5f)
        isUser -> Color.Transparent
        else -> DeepSeekCyan.copy(alpha = 0.35f)
    }

    Column(
        modifier = modifier
            .fillMaxWidth(0.92f)
            .clip(RoundedCornerShape(14.dp))
            .background(backgroundColor)
            .border(1.dp, borderColor, RoundedCornerShape(14.dp))
            .padding(horizontal = 10.dp, vertical = 8.dp)
    ) {
        Text(
            text = if (isUser) "Вы" else "Gemini",
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = if (isUser) Color(0xFF93C5FD) else DeepSeekCyan,
            modifier = Modifier.padding(bottom = 2.dp)
        )

        Text(
            text = message.content,
            fontSize = 13.sp,
            lineHeight = 16.sp,
            color = if (isError) TextError else TextPrimary
        )
    }
}
