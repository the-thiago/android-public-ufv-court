package com.ufv.court.core.schedule_service.domain.model

data class ScheduleModel(
    val ownerId: String,
    val day: String,
    val month: String,
    val year: String,
    val hourStart: Int,
    val hourEnd: Int,
    val title: String = "",
    val description: String = "",
    val scheduleType: String,
    val hasFreeSpace: Boolean = false,
    val freeSpaces: String = "",
)
