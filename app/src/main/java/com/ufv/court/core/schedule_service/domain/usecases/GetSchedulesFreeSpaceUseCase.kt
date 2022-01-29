package com.ufv.court.core.schedule_service.domain.usecases

import com.ufv.court.core.core_common.base.DispatchersProvider
import com.ufv.court.core.core_common.base.UseCase
import com.ufv.court.core.schedule_service.domain.model.ScheduleModel
import com.ufv.court.core.schedule_service.domain.repositories.ScheduleRepository
import javax.inject.Inject

class GetSchedulesFreeSpaceUseCase @Inject constructor(
    private val repository: ScheduleRepository,
    dispatchers: DispatchersProvider
) : UseCase<Unit, List<ScheduleModel>>(dispatchers.io) {

    override suspend fun execute(parameters: Unit): List<ScheduleModel> {
        return repository.getSchedulesFreeSpace()
    }
}
