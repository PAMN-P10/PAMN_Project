package com.pamn.letscook.data.repositories

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.pamn.letscook.domain.models.Image
import com.pamn.letscook.domain.models.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class UserRepository(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {
    // Sealed class for handling user-related errors
    sealed class UserError : Exception() {
        object AuthenticationError : UserError()
        object NetworkError : UserError()
        object DatabaseError : UserError()
        data class ValidationError(override val message: String) : UserError()
        data class DuplicateUserError(override val message: String) : UserError()
    }

    // User registration
    suspend fun registerUser(
        username: String,
        email: String,
        password: String
    ): Result<User> {
        return runCatching {
            withContext(Dispatchers.IO) {
                // Validate input
                require(username.isNotBlank()) { "Username cannot be empty" }
                require(email.isNotBlank() && email.contains("@")) { "Invalid email format" }
                require(password.length >= 6) { "Password must be at least 6 characters" }

                // Check if username is already taken
                val usernameExists = checkUsernameExists(username)
                if (usernameExists) {
                    throw UserError.DuplicateUserError("Username is already in use")
                }

                // Create user with Firebase Authentication
                val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
                val firebaseUser = authResult.user
                    ?: throw UserError.AuthenticationError

                // Update profile with username
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(username)
                    .build()
                firebaseUser.updateProfile(profileUpdates).await()

                // Create user document in Firestore
                val user = User(
                    userId = firebaseUser.uid,
                    username = username,
                    email = email,
                    password = "" // Never store plain text password
                )

                // Save user to Firestore
                saveUserToFirestore(user)

                user
            }
        }.onFailure { error ->
            println("User registration error: ${error.message}")
        }
    }

    // User login
    suspend fun loginUser(email: String, password: String): Result<User> {
        return runCatching {
            withContext(Dispatchers.IO) {
                // Validate input
                require(email.isNotBlank() && email.contains("@")) { "Invalid email format" }
                require(password.isNotBlank()) { "Password cannot be empty" }

                // Authenticate with Firebase
                val authResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()
                val firebaseUser = authResult.user
                    ?: throw UserError.AuthenticationError

                // Fetch user details from Firestore
                getUserById(firebaseUser.uid).getOrThrow()
            }
        }.onFailure { error ->
            println("User login error: ${error.message}")
        }
    }

    // Logout user
    fun logoutUser() {
        firebaseAuth.signOut()
    }

    // Save user to Firestore
    private suspend fun saveUserToFirestore(user: User): Result<Void> {
        return runCatching {
            withContext(Dispatchers.IO) {
                val userMap = mapOf(
                    "userId" to user.userId,
                    "username" to user.username,
                    "email" to user.email,
                    "profileImage" to (user.profileImage?.let {
                        mapOf(
                            "label" to it.label,
                            "url" to it.url,
                            "format" to it.format,
                            "width" to it.width,
                            "height" to it.height
                        )
                    } ?: emptyMap())
                )

                firestore.collection("users")
                    .document(user.userId)
                    .set(userMap)
                    .await()
            }
        }.onFailure { error ->
            println("Error saving user to Firestore: ${error.message}")
        }
    }

    // Get user by ID
    suspend fun getUserById(userId: String): Result<User> {
        return runCatching {
            withContext(Dispatchers.IO) {
                val document = firestore.collection("users")
                    .document(userId)
                    .get()
                    .await()

                if (!document.exists()) {
                    throw UserError.DatabaseError
                }

                User(
                    userId = document.getString("userId")
                        ?: throw UserError.ValidationError("Invalid user ID"),
                    username = document.getString("username")
                        ?: throw UserError.ValidationError("Invalid username"),
                    email = document.getString("email")
                        ?: throw UserError.ValidationError("Invalid email"),
                    password = "", // Never retrieve password
                    profileImage = (document.get("profileImage") as? Map<String, Any?>)?.let { imageMap ->
                        Image(
                            label = imageMap["label"] as? String ?: "",
                            url = imageMap["url"] as? String ?: "",
                            format = imageMap["format"] as? String,
                            width = (imageMap["width"] as? Number)?.toInt() ?: 0,
                            height = (imageMap["height"] as? Number)?.toInt() ?: 0
                        )
                    }
                )
            }
        }.onFailure { error ->
            println("Error retrieving user: ${error.message}")
        }
    }

    // Update user profile
    suspend fun updateUserProfile(
        userId: String,
        username: String? = null,
        email: String? = null,
        profileImage: Image? = null
    ): Result<User> {
        return runCatching {
            withContext(Dispatchers.IO) {
                val currentUser = firebaseAuth.currentUser
                    ?: throw UserError.AuthenticationError

                // Update Firebase Authentication profile if needed
                username?.let {
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(it)
                        .build()
                    currentUser.updateProfile(profileUpdates).await()
                }

                // Update email if provided
                email?.let {
                    currentUser.updateEmail(it).await()
                }

                // Prepare update map
                val updateMap = mutableMapOf<String, Any>()
                username?.let { updateMap["username"] = it }
                email?.let { updateMap["email"] = it }
                profileImage?.let {
                    updateMap["profileImage"] = mapOf(
                        "label" to it.label,
                        "url" to it.url,
                        "format" to it.format,
                        "width" to it.width,
                        "height" to it.height
                    )
                }

                // Update Firestore document
                firestore.collection("users")
                    .document(userId)
                    .update(updateMap)
                    .await()

                // Retrieve and return updated user
                getUserById(userId).getOrThrow()
            }
        }.onFailure { error ->
            println("Error updating user profile: ${error.message}")
        }
    }

    // Check if username already exists
    private suspend fun checkUsernameExists(username: String): Boolean {
        return withContext(Dispatchers.IO) {
            val snapshot = firestore.collection("users")
                .whereEqualTo("username", username)
                .get()
                .await()

            !snapshot.isEmpty
        }
    }

    // Change user password
    suspend fun changePassword(currentPassword: String, newPassword: String): Result<Void> {
        return runCatching {
            withContext(Dispatchers.IO) {
                // Validate new password
                require(newPassword.length >= 6) { "New password must be at least 6 characters" }

                // Reauthenticate user
                val currentUser = firebaseAuth.currentUser
                    ?: throw UserError.AuthenticationError

                val email = currentUser.email
                    ?: throw UserError.AuthenticationError

                // Reauthenticate before changing password
                firebaseAuth.signInWithEmailAndPassword(email, currentPassword).await()

                // Update password
                currentUser.updatePassword(newPassword).await()
            }
        }.onFailure { error ->
            println("Error changing password: ${error.message}")
        }
    }

    // Reset password
    suspend fun resetPassword(email: String): Result<Void> {
        return runCatching {
            withContext(Dispatchers.IO) {
                require(email.isNotBlank() && email.contains("@")) { "Invalid email format" }

                firebaseAuth.sendPasswordResetEmail(email).await()
            }
        }.onFailure { error ->
            println("Error sending password reset email: ${error.message}")
        }
    }

    // Get current authenticated user
    fun getCurrentUser(): User? {
        val firebaseUser = firebaseAuth.currentUser ?: return null

        // Note: This is a synchronous method and might not have the full user details
        // In a real app, you'd typically follow up with getUserById
        return User(
            userId = firebaseUser.uid,
            username = firebaseUser.displayName ?: "",
            email = firebaseUser.email ?: "",
            password = ""
        )
    }

    suspend fun saveUser(user: User): Result<Void> {
        return withContext(Dispatchers.IO) {
            runCatching {
                val userMap = mapOf(
                    "userId" to user.userId,
                    "username" to user.username,
                    "email" to user.email,
                    "profileImage" to (user.profileImage?.let {
                        mapOf(
                            "label" to it.label,
                            "url" to it.url,
                            "format" to it.format,
                            "width" to it.width,
                            "height" to it.height
                        )
                    } ?: emptyMap())
                )

                // Guardar el usuario en la colección "users" de Firestore
                firestore.collection("users")
                    .document(user.userId) // Usa el ID único del usuario como ID del documento
                    .set(userMap)
                    .await()
            }.onFailure { error ->
                println("Error saving user: ${error.message}")
            }
        }
    }
}