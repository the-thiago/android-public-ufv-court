package com.ufv.court.core.user_service.domain.usecase

import com.ufv.court.core.core_common.base.DispatchersProvider
import com.ufv.court.core.core_common.base.UseCase
import com.ufv.court.core.user_service.domain.repositories.UserRepository
import javax.inject.Inject

class ResetPasswordUseCase @Inject constructor(
    private val repository: UserRepository,
    dispatchers: DispatchersProvider
) : UseCase<ResetPasswordUseCase.Params, Unit>(dispatchers.io) {

    data class Params(val email: String)

    override suspend fun execute(parameters: Params) {
        return repository.resetPassword(email = parameters.email)
    }
}
