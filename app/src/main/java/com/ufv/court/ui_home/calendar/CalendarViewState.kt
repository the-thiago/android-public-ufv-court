package com.ufv.court.ui_home.calendar

data class CalendarViewState(
    val error: Throwable? = null,
    val isLoading: Boolean = false,
    val calendarMonths: List<CalendarMonth> = emptyList(),
    val selectedDay: CalendarDay? = null,
    val selectedMonthIndex: Int? = null
) {
    companion object {
        val Empty = CalendarViewState()
    }
}

data class CalendarMonth(
    val name: String,
    val year: String,
    val numDays: Int,
    val monthNumber: Int,
    val startDayOfWeek: Int,
    val weeks: List<CalendarWeek>,
    val alreadyLoaded: Boolean = false
)

data class CalendarWeek(val days: List<CalendarDay> = emptyList())

data class CalendarDay(
    val number: String,
    val name: String,
    val shortName: String,
    val hasEvent: Boolean = false,
    val selected: Boolean = false,
//    val events: List<EventModel> = emptyList()
)
