package com.example.streamscouter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.streamscouter.ui.screen.MainScreen
import com.example.streamscouter.ui.screen.MainViewModel
import com.example.streamscouter.ui.theme.StreamScouterTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StreamScouterTheme {
                MainScreen(MainViewModel())
            }
        }
    }
}