package com.ufv.court.core.user_service.data.repositories

import com.ufv.court.core.user_service.data.data_sources.UserDataSource
import com.ufv.court.core.user_service.domain.model.User
import com.ufv.court.core.user_service.domain.repositories.UserRepository
import javax.inject.Inject

internal class UserRepositoryImpl @Inject constructor(
    private val dataSource: UserDataSource
) : UserRepository {

    override suspend fun registerUser(email: String, password: String) {
        dataSource.registerUser(email = email, password = password)
    }

    override suspend fun sendEmailVerification() {
        dataSource.sendEmailVerification()
    }

    override suspend fun login(email: String, password: String) {
        dataSource.login(email = email, password = password)
    }

    override suspend fun isEmailVerified(): Boolean {
        return dataSource.isEmailVerified()
    }

    override suspend fun getCurrentUser(): User {
        return dataSource.getCurrentUser()
    }
}