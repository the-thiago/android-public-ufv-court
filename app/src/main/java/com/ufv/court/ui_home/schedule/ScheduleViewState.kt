package com.ufv.court.ui_home.schedule

data class ScheduleViewState(
    val error: Throwable? = null,
    val isLoading: Boolean = false,
    val date: String = "",
    val title: String = "",
    val description: String = "",
    val schedules: List<Schedule> = listOf(),
    val scheduleType: String = "",
    val hasFreeSpace: Boolean = false,
    val freeSpaces: String = "",
    val showScheduledDialog: Boolean = false
) {
    companion object {
        val Empty = ScheduleViewState()
    }
}

sealed class ScheduleError : Exception() {
    object EmptyField : ScheduleError()
    object UnselectTimeField : ScheduleError()
}
