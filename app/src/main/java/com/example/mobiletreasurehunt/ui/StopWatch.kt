package com.example.mobiletreasurehunt.ui

/**
 * Cameron Canfield
 * Mobile Dev
 * Mobile Treasure Hunt
 */

import androidx.compose.material3.Text
import androidx.compose.runtime.*
import kotlinx.coroutines.delay

@Composable
fun Stopwatch(
    isRunning: Boolean,
    onTimeUpdate: (Long) -> Unit
) {
    var time by remember { mutableStateOf(0L) }

    LaunchedEffect(isRunning) {
        while (isRunning) {
            delay(1000L)
            time += 1
            onTimeUpdate(time)
        }
    }

    Text("Time: ${time}s")
}