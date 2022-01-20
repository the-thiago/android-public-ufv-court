package com.ufv.court.ui_myschedule.scheduledetails

import com.ufv.court.core.schedule_service.domain.model.ScheduleModel
import com.ufv.court.core.user_service.domain.model.User

data class ScheduleDetailsViewState(
    val error: Throwable? = null,
    val scheduleId: String = "",
    val isLoading: Boolean = false,
    val placeholder: Boolean = true,
    val schedule: ScheduleModel? = null,
    val user: User? = null,
    val showCancellationDialog: Boolean = false,
    val showCancelledDialog: Boolean = false,
    val isTheOwner: Boolean = false
) {
    companion object {
        val Empty = ScheduleDetailsViewState()
    }
}
