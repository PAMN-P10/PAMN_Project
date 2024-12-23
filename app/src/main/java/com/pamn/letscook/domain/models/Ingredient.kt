package com.pamn.letscook.domain.models

data class Ingredient(
    val name: String,
    var quantity: Double,
    val unit: MeasurementUnit? = null,
    val type: IngredientType,
    val isAllergen: Boolean = false,
    //val imageUrl: String? = null
    val image: Image
) {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "name" to name,
            "type" to type.toString(),  // Asegúrate de que `IngredientType` tenga una representación adecuada
            "quantity" to quantity,
            "unit" to unit?.toString(),  // Asegúrate de que `MeasurementUnit` tenga una representación adecuada
            "isAllergen" to isAllergen,
            "image" to image.toMap()  // Mapea la clase `Image` si tiene una función toMap() definida
        )
    }
}
