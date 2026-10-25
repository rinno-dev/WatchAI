package com.example.watchai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.watchai.ui.navigation.WatchNavGraph
import com.example.watchai.ui.theme.WatchAiTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val app = application as WatchAiApplication
        val repository = app.chatRepository

        setContent {
            WatchAiTheme {
                WatchNavGraph(repository = repository)
            }
        }
    }
}

