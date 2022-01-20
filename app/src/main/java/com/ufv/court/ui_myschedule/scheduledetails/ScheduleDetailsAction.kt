package com.ufv.court.ui_myschedule.scheduledetails

sealed class ScheduleDetailsAction {
    object CleanErrors : ScheduleDetailsAction()
    data class ChangeShowCancellationDialog(val show: Boolean) : ScheduleDetailsAction()
    data class ChangeShowCancelledDialog(val show: Boolean) : ScheduleDetailsAction()
    object ConfirmEventCancellation : ScheduleDetailsAction()
    object ReloadData : ScheduleDetailsAction()
}
