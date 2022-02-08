package com.ufv.court.ui_home.manage

import com.ufv.court.core.schedule_service.domain.model.ScheduleModel

data class ManageViewState(
    val error: Throwable? = null,
    val placeholder: Boolean = true,
    val schedules: List<ScheduleModel> = listOf()
) {
    companion object {
        val Empty = ManageViewState()
    }
}
