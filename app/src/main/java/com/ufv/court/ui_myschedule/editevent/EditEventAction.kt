package com.ufv.court.ui_myschedule.editevent

sealed class EditEventAction {
    object CleanErrors : EditEventAction()
    data class ChangeTitle(val title: String) : EditEventAction()
    data class ChangeDescription(val description: String) : EditEventAction()
    data class ChangeEventType(val eventType: String) : EditEventAction()
    data class ChangeFreeSpaces(val freeSpaces: String) : EditEventAction()
    object UpdateEventClick : EditEventAction()
}
