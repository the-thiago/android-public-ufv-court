package com.ufv.court.core.user_service.data.data_sources

interface UserDataSource {

    suspend fun registerUser(email: String, password: String)

    suspend fun sendEmailVerification()
}