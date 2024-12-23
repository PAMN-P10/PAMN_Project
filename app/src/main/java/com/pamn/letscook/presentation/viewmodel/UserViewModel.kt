package com.pamn.letscook.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pamn_project.services.AuthService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.pamn.letscook.domain.models.Recipe
import com.pamn.letscook.domain.models.User
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class UserViewModel : ViewModel() {
    private val _currentUser = MutableLiveData<User>()
    val currentUser: LiveData<User> get() = _currentUser

    private val database = FirebaseDatabase.getInstance().reference
    private val firestore = FirebaseFirestore.getInstance()

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    val currentUserId: String?
        get() = auth.currentUser?.uid

    fun loadUser(userId: String) {
        database.child("users").child(userId).get().addOnSuccessListener { snapshot ->
            val username = snapshot.child("username").value as? String ?: ""
            val email = snapshot.child("email").value as? String ?: ""
            val favoriteRecipes = snapshot.child("favoriteRecipes").children.mapNotNull { it.value as? String }
            _currentUser.value = User(
                userId = userId,
                username = username,
                email = email,
                favoriteRecipes = favoriteRecipes.toMutableList(),
                password = TODO()
            )
        }
    }

    fun toggleFavorite(recipe: Recipe) {
        val user = _currentUser.value ?: return
        val databaseRef = database.child("users").child(user.userId).child("favoriteRecipes")

        if (user.getFavoriteRecipesList().contains(recipe.title)) {
            user.removeFavoriteRecipe(recipe)
            databaseRef.child(recipe.title).removeValue()
        } else {
            user.addFavoriteRecipe(recipe)
            databaseRef.child(recipe.title).setValue(recipe.title)
        }

        _currentUser.value = user // Emitir nuevo estado
    }

    fun getFavoriteRecipes(callback: (List<Recipe>) -> Unit) {
        val user = _currentUser.value ?: return
        val favoriteTitles = user.getFavoriteRecipesList()

        firestore.collection("recipes")
            .whereIn(FieldPath.documentId(), favoriteTitles)
            .get()
            .addOnSuccessListener { result ->
                val recipes = result.documents.mapNotNull { it.toObject(Recipe::class.java) }
                callback(recipes)
            }
    }
}
