package com.pamn.letscook.domain.models

import java.util.concurrent.TimeUnit

class Timer(
    private val duration: Int, // in seconds
    private val timeUnit: TimeUnit = TimeUnit.SECONDS
) {
    private var remainingTime: Int = duration
    private var isRunning: Boolean = false

    // POR COMPLETAR LAS FUNCIONES ----------------

    val isPaused: Boolean
        get() = !isRunning

    val totalDuration: Int
        get() = duration

    fun start() { isRunning = true }

    fun pause() { isRunning = false }

    fun reset() {
        remainingTime = duration
        isRunning = false
    }

    fun getRemainingTime(): Int = remainingTime
}