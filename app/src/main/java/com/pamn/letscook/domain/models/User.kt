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
    private val favoriteRecipes: MutableList<Recipe> = mutableListOf(),
    private val userRecipes: MutableList<Recipe> = mutableListOf()
) {
    // Métodos encapsulan las listas para cumplir con el principio de responsabilidad única
    // Evitar accesos directos a las listas mutables
    fun getFavoriteRecipes(): List<Recipe> = favoriteRecipes
    fun getUserRecipes(): List<Recipe> = userRecipes

    fun addFavoriteRecipe(recipe: Recipe) {
        if (!favoriteRecipes.contains(recipe)) {
            favoriteRecipes.add(recipe)
        }
        //if (recipe !in favoriteRecipes) favoriteRecipes.add(recipe)
    }

    fun removeFavoriteRecipe(recipe: Recipe) {
        favoriteRecipes.remove(recipe)
    }

    //pasarlos a casos de uso
    /**
    fun updateProfile(
        newUsername: String? = null,
        newEmail: String? = null,
        newProfileImage: String? = null
    ) {
        newUsername?.let { username = it }
        newEmail?.let { email = it }
        newProfileImage?.let { profileImageUrl = it }
    }
    */
}