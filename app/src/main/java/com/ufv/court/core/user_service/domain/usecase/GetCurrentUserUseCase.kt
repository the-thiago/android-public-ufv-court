package com.ufv.court.core.user_service.domain.usecase

import com.ufv.court.core.core_common.base.DispatchersProvider
import com.ufv.court.core.core_common.base.UseCase
import com.ufv.court.core.user_service.domain.model.UserModel
import com.ufv.court.core.user_service.domain.repositories.UserRepository
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val repository: UserRepository,
    dispatchers: DispatchersProvider
) : UseCase<Unit, UserModel>(dispatchers.io) {

    override suspend fun execute(parameters: Unit): UserModel {
        return repository.getCurrentUser()
    }
}