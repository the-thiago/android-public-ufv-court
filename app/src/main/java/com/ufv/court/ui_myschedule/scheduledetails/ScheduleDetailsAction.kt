package com.ufv.court.ui_myschedule.scheduledetails

sealed class ScheduleDetailsAction {
    object CleanErrors : ScheduleDetailsAction()
    data class ChangeShowCancellationDialog(val show: Boolean) : ScheduleDetailsAction()
    data class ChangeShowCancelledDialog(val show: Boolean) : ScheduleDetailsAction()
    data class ChangeShowParticipatingDialog(val show: Boolean) : ScheduleDetailsAction()
    data class ChangeShowCancelParticipationDialog(val show: Boolean) : ScheduleDetailsAction()
    object ConfirmEventCancellation : ScheduleDetailsAction()
    object ReloadData : ScheduleDetailsAction()
    object ParticipateClick : ScheduleDetailsAction()
    object SendComment : ScheduleDetailsAction()
    data class ChangeComment(val comment: String) : ScheduleDetailsAction()
}
