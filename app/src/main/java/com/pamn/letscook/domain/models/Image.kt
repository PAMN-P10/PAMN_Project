package com.pamn.letscook.domain.models

data class Image(
    val label: String,
    val url: String,
    // Optionals:
    val format: String? = null,
    val width: Int? = null,
    val height: Int? = null
) {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "label" to label,
            "url" to url,
            "format" to format,
            "width" to width,
            "height" to height
        )
    }
}
