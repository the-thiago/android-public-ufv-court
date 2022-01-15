package com.ufv.court.ui_home.schedule

data class ScheduleViewState(
    val error: Throwable? = null,
    val isLoading: Boolean = false,
    val date: String = "",
    val schedules: List<Schedule> = listOf()
) {
    companion object {
        val Empty = ScheduleViewState()
    }
}
