package com.ufv.court.core.schedule_service.data.data_sources

import com.ufv.court.core.schedule_service.domain.model.ScheduleModel

interface ScheduleDataSource {

    suspend fun createSchedule(schedule: ScheduleModel)
}