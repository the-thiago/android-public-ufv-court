package com.ufv.court.ui_myschedule.scheduledetails

import com.ufv.court.core.schedule_service.domain.model.ScheduleModel
import com.ufv.court.core.user_service.domain.model.User

data class ScheduleDetailsViewState(
    val error: Throwable? = null,
    val isLoading: Boolean = false,
    val schedule: ScheduleModel? = null,
    val user: User? = null
) {
    companion object {
        val Empty = ScheduleDetailsViewState()
    }
}
