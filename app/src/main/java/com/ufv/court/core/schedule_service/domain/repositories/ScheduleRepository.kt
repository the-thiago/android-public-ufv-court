package com.ufv.court.core.schedule_service.domain.repositories

import com.ufv.court.core.schedule_service.domain.model.ScheduleModel

interface ScheduleRepository {

    suspend fun createSchedule(schedule: ScheduleModel)

    suspend fun getScheduleByDayUseCase(
        day: String,
        month: String,
        year: String
    ): List<ScheduleModel>
}
