package com.pamn.letscook.domain.useCases

import com.pamn.letscook.domain.models.Image
import com.pamn.letscook.domain.models.User

class UpdateUserProfileUseCase {
    operator fun invoke(user: User, newUsername: String?, newEmail: String?, newProfileImage: Image?) {
        newUsername?.let { user.username = it }
        newEmail?.let { user.email = it }
        newProfileImage?.let { user.profileImage = it }
    }
}
