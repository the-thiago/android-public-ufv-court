package com.ufv.court.ui_home.schedule

sealed class ScheduleAction {
    object CleanErrors : ScheduleAction()
    data class ScheduleClick(val index: Int) : ScheduleAction()
}
