package com.example.cacatesouro

import android.annotation.SuppressLint
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class TimerViewModel : ViewModel() {
    var startTime = mutableStateOf<Long?>(null)
        private set

    var totalTime = mutableStateOf<Long>(0)
        private set

    fun startTimer() {
        startTime.value = System.currentTimeMillis()
    }

    fun calculateTotalTime() {
        startTime.value?.let {
            totalTime.value = (System.currentTimeMillis() - it) / 1000
        }
    }

    fun resetTimer() {
        startTime.value = null
        totalTime.value = 0
    }

    @SuppressLint("DefaultLocale")
    fun formattedTime(): String {
        val minutes = totalTime.value / 60
        val remainingSeconds = totalTime.value % 60
        return String.format("%02d:%02d", minutes, remainingSeconds)
    }
}