package com.ufv.court.ui_home.manage

import com.ufv.court.core.schedule_service.domain.model.ScheduleModel

data class ManageViewState(
    val error: Throwable? = null,
    val placeholder: Boolean = true,
    val isLoading: Boolean = false,
    val allSchedules: List<ScheduleModel> = listOf(),
    val schedulesByDate: List<ScheduleModel> = listOf(),
    val selectedDate: String = ""
) {
    companion object {
        val Empty = ManageViewState()
    }
}
