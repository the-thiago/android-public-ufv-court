package com.ufv.court.core.user_service.domain.repositories

import com.ufv.court.core.user_service.domain.model.User

interface UserRepository {

    suspend fun registerUser(email: String, password: String)

    suspend fun sendEmailVerification()

    suspend fun login(email: String, password: String)

    suspend fun isEmailVerified(): Boolean

    suspend fun getCurrentUser(): User
}