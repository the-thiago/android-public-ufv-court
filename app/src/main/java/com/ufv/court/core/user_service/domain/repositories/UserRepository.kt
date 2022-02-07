package com.ufv.court.core.user_service.domain.repositories

import android.net.Uri
import com.ufv.court.core.user_service.data_remote.request.RegisterUser
import com.ufv.court.core.user_service.domain.model.UserModel

interface UserRepository {

    suspend fun registerUser(user: RegisterUser)

    suspend fun sendEmailVerification()

    suspend fun login(email: String, password: String)

    suspend fun isEmailVerified(): Boolean

    suspend fun getCurrentUser(): UserModel

    suspend fun getUsers(ids: List<String>): List<UserModel>

    suspend fun logout()

    suspend fun changePassword(oldPassword: String, newPassword: String)

    suspend fun resetPassword(email: String)

    suspend fun updateUser(user: UserModel, imageUri: Uri?)
}
