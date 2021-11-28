package com.ufv.court.core.user_service.data.repositories

import com.ufv.court.core.user_service.data.data_sources.UserDataSource
import com.ufv.court.core.user_service.domain.repositories.UserRepository
import javax.inject.Inject

internal class UserRepositoryImpl @Inject constructor(
    private val dataSource: UserDataSource
) : UserRepository {

    override suspend fun registerUser(email: String, password: String) {
        dataSource.registerUser(email = email, password = password)
    }
}