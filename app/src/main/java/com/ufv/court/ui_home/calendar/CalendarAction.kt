package com.ufv.court.ui_home.calendar

sealed class CalendarAction {
    object CleanErrors : CalendarAction()
    data class LoadEventsOfMonth(val index: Int) : CalendarAction()
    data class ChangeSelectedDay(
        val day: CalendarDay?, val monthIndex: Int?
    ) : CalendarAction()
}
