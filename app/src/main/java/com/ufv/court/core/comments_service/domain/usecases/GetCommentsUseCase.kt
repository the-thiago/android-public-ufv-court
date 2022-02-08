package com.ufv.court.core.comments_service.domain.usecases

import com.ufv.court.core.comments_service.domain.model.ScheduleComments
import com.ufv.court.core.comments_service.domain.repositories.CommentsRepository
import com.ufv.court.core.core_common.base.DispatchersProvider
import com.ufv.court.core.core_common.base.UseCase
import javax.inject.Inject

class GetCommentsUseCase @Inject constructor(
    private val repository: CommentsRepository,
    dispatchers: DispatchersProvider
) : UseCase<GetCommentsUseCase.Params, ScheduleComments>(dispatchers.io) {

    data class Params(val eventId: String)

    override suspend fun execute(parameters: Params): ScheduleComments {
        return repository.getComments(eventId = parameters.eventId)
    }
}
