package com.pamn.letscook.domain.models

data class Image(
    val label: String,
    val url: String,
    //Optionals:
    val format: String? = null,
    val with: Int? = null,
    val height: Int? = null
)
