package com.ufv.court.core.user_service.domain.usecase

import com.ufv.court.core.core_common.base.DispatchersProvider
import com.ufv.court.core.core_common.base.UseCase
import com.ufv.court.core.user_service.domain.repositories.UserRepository
import javax.inject.Inject

class ChangePasswordUseCase @Inject constructor(
    private val repository: UserRepository,
    dispatchers: DispatchersProvider
) : UseCase<ChangePasswordUseCase.Params, Unit>(dispatchers.io) {

    data class Params(val oldPassword: String, val newPassword: String)

    override suspend fun execute(parameters: Params) {
        return repository.changePassword(
            oldPassword = parameters.oldPassword, newPassword = parameters.newPassword
        )
    }
}