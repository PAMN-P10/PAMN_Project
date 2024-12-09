package com.pamn.letscook.domain.models

data class Ingredient(
    val name: String,
    val type: IngredientType,
    var quantity: Double,
    //val unit: MeasurementUnit,
    val isAllergen: Boolean = false,
    //val imageUrl: String? = null
    val image: Image
){

}
