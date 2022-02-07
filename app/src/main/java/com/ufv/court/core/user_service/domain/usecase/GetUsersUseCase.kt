package com.ufv.court.core.user_service.domain.usecase

import com.ufv.court.core.core_common.base.DispatchersProvider
import com.ufv.court.core.core_common.base.UseCase
import com.ufv.court.core.user_service.domain.model.UserModel
import com.ufv.court.core.user_service.domain.repositories.UserRepository
import javax.inject.Inject

class GetUsersUseCase @Inject constructor(
    private val repository: UserRepository,
    dispatchers: DispatchersProvider
) : UseCase<GetUsersUseCase.Params, List<UserModel>>(dispatchers.io) {

    data class Params(val ids: List<String>)

    override suspend fun execute(parameters: Params): List<UserModel> {
        return repository.getUsers(ids = parameters.ids)
    }
}
