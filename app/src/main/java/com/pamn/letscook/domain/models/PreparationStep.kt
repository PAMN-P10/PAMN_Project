package com.pamn.letscook.domain.models

data class PreparationStep(
    val stepNumber: Int,
    val description: String,
    val estimatedTime: Timer,
    val requiresTimer: Boolean = false,
    val imageUrl: String? = null
) {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "stepNumber" to stepNumber,
            "description" to description,
            "estimatedTime" to estimatedTime.toString(),  // O usa un formato adecuado para Timer si es necesario
            "requiresTimer" to requiresTimer,
            "imageUrl" to imageUrl
        )
    }
}