package com.ufv.court.core.schedule_service.domain.usecases

import com.ufv.court.core.core_common.base.DispatchersProvider
import com.ufv.court.core.core_common.base.UseCase
import com.ufv.court.core.schedule_service.domain.model.ScheduleModel
import com.ufv.court.core.schedule_service.domain.repositories.ScheduleRepository
import javax.inject.Inject

class UpdateScheduleUseCase @Inject constructor(
    private val repository: ScheduleRepository,
    dispatchers: DispatchersProvider
) : UseCase<UpdateScheduleUseCase.Params, Unit>(dispatchers.io) {

    data class Params(val id: String, val newSchedule: ScheduleModel)

    override suspend fun execute(parameters: Params) {
        return repository.updateSchedule(id = parameters.id, newSchedule = parameters.newSchedule)
    }
}
