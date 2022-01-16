package com.ufv.court.ui_home.schedule

import com.ufv.court.R

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
    val showScheduledDialog: Boolean = true
) {
    companion object {
        val Empty = ScheduleViewState()
    }
}

enum class ScheduleType(val strId: Int) {
    SOCCER(R.string.soccer),
    VOLLEY(R.string.volley),
    BASKET(R.string.basket),
    EVENT(R.string.event),
    OTHER(R.string.other),
    UNSELECTED(R.string.unselected)
}

sealed class ScheduleError : Exception() {
    object EmptyField : ScheduleError()
    object UnselectTimeField : ScheduleError()
}
