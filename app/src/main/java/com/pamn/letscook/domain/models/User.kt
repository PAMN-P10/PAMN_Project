package com.pamn.letscook.domain.models

data class User(
    val userId: String,
    var username: String,
    var email: String,
    private var password: String,
    //var profileImageUrl: String? = null,
    var profileImage: Image? = null,
    //val dietaryPreferences: MutableList<String> = mutableListOf(),
    //Encapsuled: para cumplir con el principio de responsabilidad única
    var favoriteRecipes: MutableList<String> = mutableListOf()
) {
    // Métodos encapsulan las listas para cumplir con el principio de responsabilidad única
    // Evitar accesos directos a las listas mutables
    fun getFavoriteRecipesList(): List<String> = favoriteRecipes

    fun addFavoriteRecipe(recipe: Recipe) {
        if (!favoriteRecipes.contains(recipe.title)) {
            favoriteRecipes.add(recipe.title)
        }
    }

    fun removeFavoriteRecipe(recipe: Recipe) {
        favoriteRecipes.remove(recipe.title)
    }

}