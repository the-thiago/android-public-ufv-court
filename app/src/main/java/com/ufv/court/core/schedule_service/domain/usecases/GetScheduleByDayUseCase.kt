package com.ufv.court.core.schedule_service.domain.usecases

import com.ufv.court.core.core_common.base.DispatchersProvider
import com.ufv.court.core.core_common.base.UseCase
import com.ufv.court.core.schedule_service.domain.model.ScheduleModel
import com.ufv.court.core.schedule_service.domain.repositories.ScheduleRepository
import javax.inject.Inject

class GetScheduleByDayUseCase @Inject constructor(
    private val repository: ScheduleRepository,
    dispatchers: DispatchersProvider
) : UseCase<GetScheduleByDayUseCase.Params, List<ScheduleModel>>(dispatchers.io) {

    data class Params(val day: String, val month: String, val year: String)

    override suspend fun execute(parameters: Params): List<ScheduleModel> {
        return repository.getScheduleByDay(
            day = parameters.day,
            month = parameters.month,
            year = parameters.year
        )
    }
}
