package com.ufv.court.ui_myschedule.myschedule

import com.ufv.court.core.schedule_service.domain.model.ScheduleModel

data class MyScheduleViewState(
    val error: Throwable? = null,
    val scheduled: List<ScheduleModel> = listOf(),
    val scheduledAsParticipant: List<ScheduleModel> = listOf(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false
) {
    companion object {
        val Empty = MyScheduleViewState()
    }
}
