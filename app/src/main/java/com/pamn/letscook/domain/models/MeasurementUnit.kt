package com.pamn.letscook.domain.models

enum class MeasurementUnit(val displayName: String) {
    GRAMS("g"),
    KILOGRAMS("kg"),
    MILLILITERS("ml"),
    LITERS("l"),
    CUPS("cups"),
    TABLESPOONS("tbsp"),
    TEASPOONS("tsp"),
    PIECES("piece"),
    DROP("drop");


    override fun toString(): String {
        return displayName
    }
}