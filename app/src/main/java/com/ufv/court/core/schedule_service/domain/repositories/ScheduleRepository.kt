package com.ufv.court.core.schedule_service.domain.repositories

import com.ufv.court.core.schedule_service.domain.model.ScheduleModel

interface ScheduleRepository {

    suspend fun createSchedule(schedule: ScheduleModel)

    suspend fun getScheduleByDay(
        day: String,
        month: String,
        year: String
    ): List<ScheduleModel>

    suspend fun getScheduledByUser(): List<ScheduleModel>

    suspend fun getSchedule(id: String): ScheduleModel
}
