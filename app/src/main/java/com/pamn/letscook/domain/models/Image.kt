package com.pamn.letscook.domain.models

data class Image(
    val label: String,
    val url: String,
    //Optionals:
    val format: String? = null,
    val width: Int? = null,
    val height: Int? = null
)
