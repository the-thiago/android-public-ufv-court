package com.ufv.court.core.user_service.domain.repositories

interface UserRepository {

    suspend fun registerUser(email: String, password: String)

    suspend fun sendEmailVerification()
}