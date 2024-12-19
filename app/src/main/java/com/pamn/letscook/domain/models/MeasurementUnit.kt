package com.pamn.letscook.domain.models

/**
 * GRAM("g"),
 *     KILOGRAM("kg"),
 *     LITER("l"),
 *     MILLILITER("ml"),
 *     CUP("cup"),
 *     TEASPOON("tsp"),
 *     TABLESPOON("tbsp"),
 *     PIECE("piece"),
 *     SLICE("slice"),
 *     PINCH("pinch"),
 *     DROP("drop");
 */
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