package com.pamn.letscook.domain.models

data class PreparationStep(
    val stepNumber: Int,
    val description: String,
    val estimatedTime: Timer,
    val requiresTimer: Boolean = false,
    val imageUrl: String? = null
){

}
