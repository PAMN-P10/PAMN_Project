package com.pamn.letscook.domain.models


// Rating model for recipe feedback
data class Rating(
    val user: User,
    val score: Int, // 1-5
    val comment: String? = null,
    val ratingDate: java.util.Date = java.util.Date()
)