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

    override suspend fun getScheduleByDay(
        day: String,
        month: String,
        year: String
    ): List<ScheduleModel> {
        return dataSource.getScheduleByDay(day = day, month = month, year = year)
    }

    override suspend fun getScheduledByUser(): List<ScheduleModel> {
        return dataSource.getScheduledByUser()
    }

    override suspend fun getSchedule(id: String): ScheduleModel {
        return dataSource.getSchedule(id = id)
    }

    override suspend fun updateSchedule(id: String, newSchedule: ScheduleModel) {
        return dataSource.updateSchedule(id = id, newSchedule = newSchedule)
    }
}
