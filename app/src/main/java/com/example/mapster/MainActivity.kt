package com.example.mapster

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                var showSplash by remember { mutableStateOf(true) }

                if (showSplash) {
                    SplashScreen(
                        onSplashComplete = { showSplash = false }
                    )
                } else {
                    QRScannerApp()
                }
            }
        }
    }
}

