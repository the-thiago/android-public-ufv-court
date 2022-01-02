package com.ufv.court.core.user_service.data.data_sources

import com.ufv.court.core.user_service.domain.model.User

interface UserDataSource {

    suspend fun registerUser(email: String, password: String, name: String)

    suspend fun sendEmailVerification()

    suspend fun login(email: String, password: String)

    suspend fun isEmailVerified(): Boolean

    suspend fun getCurrentUser(): User

    suspend fun logout()

    suspend fun changePassword(oldPassword: String, newPassword: String)

    suspend fun resetPassword(email: String)
}