package com.ufv.court.core.schedule_service.data.repositories

import com.ufv.court.core.schedule_service.data.data_sources.ScheduleDataSource
import com.ufv.court.core.schedule_service.domain.model.ScheduleModel
import com.ufv.court.core.schedule_service.domain.repositories.ScheduleRepository
import javax.inject.Inject

internal class ScheduleRepositoryImpl @Inject constructor(
    private val dataSource: ScheduleDataSource
) : ScheduleRepository {

    override suspend fun createSchedule(schedule: ScheduleModel) {
        dataSource.createSchedule(schedule = schedule)
    }
}