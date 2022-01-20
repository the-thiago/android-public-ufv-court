package com.ufv.court.core.user_service.domain.usecase

import android.net.Uri
import com.ufv.court.core.core_common.base.DispatchersProvider
import com.ufv.court.core.core_common.base.UseCase
import com.ufv.court.core.user_service.domain.model.UserModel
import com.ufv.court.core.user_service.domain.repositories.UserRepository
import javax.inject.Inject

class UpdateUserUseCase @Inject constructor(
    private val repository: UserRepository,
    dispatchers: DispatchersProvider
) : UseCase<UpdateUserUseCase.Params, Unit>(dispatchers.io) {

    data class Params(val user: UserModel, val imageUri: Uri?)

    override suspend fun execute(parameters: Params) {
        repository.updateUser(user = parameters.user, imageUri = parameters.imageUri)
    }
}
