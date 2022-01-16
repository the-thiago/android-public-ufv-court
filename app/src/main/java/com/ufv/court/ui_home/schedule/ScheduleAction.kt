package com.ufv.court.ui_home.schedule

sealed class ScheduleAction {
    object CleanErrors : ScheduleAction()
    data class ScheduleTimeClick(val index: Int) : ScheduleAction()
    data class ChangeTitle(val title: String) : ScheduleAction()
    data class ChangeDescription(val description: String) : ScheduleAction()
    data class ChangeScheduleType(val type: String) : ScheduleAction()
    data class ChangeHasFreeSpace(val has: Boolean) : ScheduleAction()
    data class ChangeFreeSpace(val freeSpace: String) : ScheduleAction()
    object CreateScheduleClick : ScheduleAction()
}
