package com.ufv.court.core.user_service.domain.usecase

import com.ufv.court.core.core_common.base.DispatchersProvider
import com.ufv.court.core.core_common.base.UseCase
import com.ufv.court.core.user_service.domain.repositories.UserRepository
import javax.inject.Inject

class RegisterUserUseCase @Inject constructor(
    private val repository: UserRepository,
    dispatchers: DispatchersProvider
) : UseCase<RegisterUserUseCase.Params, Unit>(dispatchers.io) {

    data class Params(val email: String, val password: String)

    override suspend fun execute(parameters: Params) {
        return repository.registerUser(email = parameters.email, password = parameters.password)
    }
}