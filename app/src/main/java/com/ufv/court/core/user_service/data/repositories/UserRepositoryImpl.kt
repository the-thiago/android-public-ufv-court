package com.ufv.court.core.user_service.data.repositories

import com.ufv.court.core.user_service.data.data_sources.UserDataSource
import com.ufv.court.core.user_service.data_remote.request.RegisterUser
import com.ufv.court.core.user_service.domain.model.UserModel
import com.ufv.court.core.user_service.domain.repositories.UserRepository
import javax.inject.Inject

internal class UserRepositoryImpl @Inject constructor(
    private val dataSource: UserDataSource
) : UserRepository {

    override suspend fun registerUser(user: RegisterUser) {
        dataSource.registerUser(userRequest = user)
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

    override suspend fun getCurrentUser(): UserModel {
        return dataSource.getCurrentUser()
    }

    override suspend fun logout() {
        dataSource.logout()
    }

    override suspend fun changePassword(oldPassword: String, newPassword: String) {
        dataSource.changePassword(oldPassword = oldPassword, newPassword = newPassword)
    }

    override suspend fun resetPassword(email: String) {
        dataSource.resetPassword(email = email)
    }
}