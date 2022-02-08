package com.ufv.court.core.schedule_service.domain.repositories

import com.ufv.court.core.schedule_service.domain.model.ScheduleModel

interface ScheduleRepository {

    suspend fun createSchedule(schedule: ScheduleModel): String

    suspend fun getScheduleByDay(timeInMillis: Long): List<ScheduleModel>

    suspend fun getAllScheduleAfterDate(timeInMillis: Long): List<ScheduleModel>

    suspend fun getScheduledByUser(): List<ScheduleModel>

    suspend fun getScheduledAsParticipant(): List<ScheduleModel>

    suspend fun getSchedule(id: String): ScheduleModel

    suspend fun updateSchedule(id: String, newSchedule: ScheduleModel)

    suspend fun getSchedulesFreeSpace(): List<ScheduleModel>
}
