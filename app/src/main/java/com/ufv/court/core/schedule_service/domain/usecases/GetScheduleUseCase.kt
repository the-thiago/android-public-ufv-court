package com.ufv.court.core.schedule_service.domain.usecases

import com.ufv.court.core.core_common.base.DispatchersProvider
import com.ufv.court.core.core_common.base.UseCase
import com.ufv.court.core.schedule_service.domain.model.ScheduleModel
import com.ufv.court.core.schedule_service.domain.repositories.ScheduleRepository
import javax.inject.Inject

class GetScheduleUseCase @Inject constructor(
    private val repository: ScheduleRepository,
    dispatchers: DispatchersProvider
) : UseCase<GetScheduleUseCase.Params, ScheduleModel>(dispatchers.io) {

    data class Params(val id: String)

    override suspend fun execute(parameters: Params): ScheduleModel {
        return repository.getSchedule(id = parameters.id)
    }
}
