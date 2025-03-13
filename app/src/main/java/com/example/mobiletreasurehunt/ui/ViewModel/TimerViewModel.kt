package com.example.mobiletreasurehunt.ui

/**
 * Cameron Canfield
 * Mobile Dev
 * Mobile Treasure Hunt
 */

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TimerViewModel : ViewModel() {
    private val _elapsedTime = MutableStateFlow(0L)
    val elapsedTime = _elapsedTime.asStateFlow() // Expose as read-only flow

    private var timerJob: Job? = null

    fun startTimer() {
        if (timerJob == null) {
            Log.d("TimerViewModel", "Starting timer from ${_elapsedTime.value}")
            timerJob = viewModelScope.launch {
                while (true) {
                    delay(1000L)
                    _elapsedTime.value += 1 // Triggers UI recomposition
                    Log.d("TimerViewModel", "Timer running: ${_elapsedTime.value}s")
                }
            }
        }
    }

    fun stopTimer() {
        Log.d("TimerViewModel", "Stopping timer at ${_elapsedTime.value}s")
        timerJob?.cancel()
        timerJob = null
    }

    fun resetTimer() {
        _elapsedTime.value = 0L
        stopTimer()
    }
}
