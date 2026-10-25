package com.example.watchai.ui.components

import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.rotary.onRotaryScrollEvent
import androidx.wear.compose.material.ScalingLazyListState
import kotlinx.coroutines.launch


@Composable
fun Modifier.attachRotaryScroll(
    scrollState: ScalingLazyListState,
    focusRequester: FocusRequester = remember { FocusRequester() }
): Modifier {
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    return this
        .focusRequester(focusRequester)
        .focusable()
        .onRotaryScrollEvent { event ->
            coroutineScope.launch {
                scrollState.scrollBy(event.verticalScrollPixels)
            }
            true
        }
}
