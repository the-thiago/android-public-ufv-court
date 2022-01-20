package com.ufv.court.ui_myschedule.editevent

import com.ufv.court.core.schedule_service.domain.model.ScheduleModel

data class EditEventViewState(
    val error: Throwable? = null,
    val schedule: ScheduleModel? = null,
    val remainingFreeSpaces: String = "",
    val isLoading: Boolean = false,
    val placeholder: Boolean = true,
    val showSavedDialog: Boolean = false
) {
    companion object {
        val Empty = EditEventViewState()
    }
}

sealed class EditEventError : Exception() {
    object EmptyField : EditEventError()
}
