package com.ufv.court.core.user_service.data.data_sources

import com.ufv.court.core.user_service.data_remote.request.RegisterUser
import com.ufv.court.core.user_service.domain.model.UserModel

interface UserDataSource {

    suspend fun registerUser(userRequest: RegisterUser)

    suspend fun sendEmailVerification()

    suspend fun login(email: String, password: String)

    suspend fun isEmailVerified(): Boolean

    suspend fun getCurrentUser(): UserModel

    suspend fun logout()

    suspend fun changePassword(oldPassword: String, newPassword: String)

    suspend fun resetPassword(email: String)
}