package com.ufv.court.core.user_service.domain.usecase

import com.ufv.court.core.core_common.base.DispatchersProvider
import com.ufv.court.core.core_common.base.UseCase
import com.ufv.court.core.user_service.domain.repositories.UserRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: UserRepository,
    dispatchers: DispatchersProvider
) : UseCase<LoginUseCase.Params, Unit>(dispatchers.io) {

    data class Params(val email: String, val password: String)

    override suspend fun execute(parameters: Params) {
        return repository.login(email = parameters.email, password = parameters.password)
    }
}