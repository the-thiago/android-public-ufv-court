package com.ufv.court.core.comments_service.domain.usecases

import com.ufv.court.core.comments_service.domain.model.ScheduleComments
import com.ufv.court.core.comments_service.domain.repositories.CommentsRepository
import com.ufv.court.core.core_common.base.DispatchersProvider
import com.ufv.court.core.core_common.base.UseCase
import javax.inject.Inject

class CreateEventCommentsUseCase @Inject constructor(
    private val repository: CommentsRepository,
    dispatchers: DispatchersProvider
) : UseCase<CreateEventCommentsUseCase.Params, Unit>(dispatchers.io) {

    data class Params(val eventComments: ScheduleComments)

    override suspend fun execute(parameters: Params) {
        return repository.createEventComments(eventComments = parameters.eventComments)
    }
}
