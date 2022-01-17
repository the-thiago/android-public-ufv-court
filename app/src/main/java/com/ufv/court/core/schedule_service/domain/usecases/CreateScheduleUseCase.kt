package com.ufv.court.core.schedule_service.domain.usecases

import com.ufv.court.core.core_common.base.DispatchersProvider
import com.ufv.court.core.core_common.base.UseCase
import com.ufv.court.core.schedule_service.domain.model.ScheduleModel
import com.ufv.court.core.schedule_service.domain.repositories.ScheduleRepository
import javax.inject.Inject

class CreateScheduleUseCase @Inject constructor(
    private val repository: ScheduleRepository,
    dispatchers: DispatchersProvider
) : UseCase<CreateScheduleUseCase.Params, Unit>(dispatchers.io) {

    data class Params(val schedule: ScheduleModel)

    override suspend fun execute(parameters: Params) {
        return repository.createSchedule(schedule = parameters.schedule)
    }
}
