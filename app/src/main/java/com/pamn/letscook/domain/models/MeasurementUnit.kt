package com.pamn.letscook.domain.models

enum class MeasurementUnit(val displayName: String) {
    GRAM("g"),
    KILOGRAM("kg"),
    LITER("l"),
    MILLILITER("ml"),
    CUP("cup"),
    TEASPOON("tsp"),
    TABLESPOON("tbsp"),
    PIECE("piece"),
    SLICE("slice"),
    PINCH("pinch"),
    DROP("drop");

    override fun toString(): String {
        return displayName
    }
}
