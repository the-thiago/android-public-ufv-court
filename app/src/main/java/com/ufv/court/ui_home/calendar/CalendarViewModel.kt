package com.ufv.court.ui_home.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(

) : ViewModel() {

    private val pendingActions = MutableSharedFlow<CalendarAction>()

    private val _state: MutableStateFlow<CalendarViewState> =
        MutableStateFlow(CalendarViewState.Empty)
    val state: StateFlow<CalendarViewState> = _state

    init {
        getTodayDate()
        setupCalendarMonths()
        handleActions()
    }

    private fun getTodayDate() {
        viewModelScope.launch {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = System.currentTimeMillis()
            _state.value = state.value.copy(
                todayDay = calendar.get(Calendar.DAY_OF_MONTH),
                todayMonth = calendar.get(Calendar.MONTH) + 1,
                todayYear = calendar.get(Calendar.YEAR)
            )
        }
    }

    private fun setupCalendarMonths() {
        viewModelScope.launch {
            val calendarMonths = mutableListOf<CalendarMonth>()
            val calendar = Calendar.getInstance()
            calendar.time = Date()
            calendar.set(Calendar.DAY_OF_MONTH, 1)
            repeat(AMOUNT_OF_MONTH) {
                val startDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
                val numDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
                val weeks = mutableListOf<CalendarWeek>()
                var daysOfMonth = (1..numDays).toList()
                val firstWeek = getFirstWeekOfMonth(daysOfMonth, startDayOfWeek, calendar)
                weeks.add(firstWeek)
                daysOfMonth = daysOfMonth.drop(NUMBER_OF_DAYS_IN_A_WEEK_PLUS_ONE - startDayOfWeek)
                weeks.addAll(getRemainingWeeksOfMonth(daysOfMonth, calendar))
                val month = CalendarMonth(
                    name = calendar.getDisplayName(
                        Calendar.MONTH,
                        Calendar.LONG,
                        Locale.getDefault()
                    )?.replaceFirstChar { it.uppercase() } ?: "",
                    year = calendar.get(Calendar.YEAR).toString(),
                    numDays = numDays,
                    monthNumber = calendar.get(Calendar.MONTH) + 1,
                    startDayOfWeek = startDayOfWeek,
                    weeks = weeks
                )
                calendarMonths.add(month)
                calendar.set(Calendar.DAY_OF_MONTH, 1)
                calendar.add(Calendar.MONTH, 1) // next month
            }
            _state.value = state.value.copy(calendarMonths = calendarMonths)
        }
    }

    private fun getFirstWeekOfMonth(
        daysOfMonth: List<Int>,
        startDayOfWeek: Int,
        calendar: Calendar
    ): CalendarWeek {
        val daysOfFirstWeek = daysOfMonth
            .take(NUMBER_OF_DAYS_IN_A_WEEK_PLUS_ONE - startDayOfWeek).map {
                calendar.set(Calendar.DAY_OF_MONTH, it)
                CalendarDay(
                    number = it.toString(),
                    name = calendar.getDisplayName(
                        Calendar.DAY_OF_WEEK,
                        Calendar.LONG,
                        Locale.getDefault()
                    ) ?: "",
                    shortName = calendar.getDisplayName(
                        Calendar.DAY_OF_WEEK,
                        Calendar.SHORT,
                        Locale.getDefault()
                    ) ?: ""
                )
            }
        return CalendarWeek(days = daysOfFirstWeek)
    }

    private fun getRemainingWeeksOfMonth(
        daysOfMonth: List<Int>,
        calendar: Calendar
    ): List<CalendarWeek> {
        val remainingWeeks = mutableListOf<CalendarWeek>()
        daysOfMonth.chunked(NUMBER_OF_DAYS_IN_A_WEEK).forEach { week ->
            val calendarWeek = CalendarWeek(week.map {
                calendar.set(Calendar.DAY_OF_MONTH, it)
                CalendarDay(
                    number = it.toString(),
                    name = calendar.getDisplayName(
                        Calendar.DAY_OF_WEEK,
                        Calendar.LONG,
                        Locale.getDefault()
                    ) ?: "",
                    shortName = calendar.getDisplayName(
                        Calendar.DAY_OF_WEEK,
                        Calendar.SHORT,
                        Locale.getDefault()
                    ) ?: ""
                )
            })
            remainingWeeks.add(calendarWeek)
        }
        return remainingWeeks
    }

    private fun handleActions() {
        viewModelScope.launch {
            pendingActions.collect { action ->
                when (action) {
                    CalendarAction.CleanErrors -> _state.value = state.value.copy(error = null)
                    is CalendarAction.ChangeSelectedDay -> {
                        changeSelectedDay(selectedDay = action.day, monthIndex = action.monthIndex)
                    }
                    is CalendarAction.LoadEventsOfMonth -> {

                    }
                }
            }
        }
    }

    private fun changeSelectedDay(selectedDay: CalendarDay?, monthIndex: Int?) {
        viewModelScope.launch {
            unselectLastSelectedDay()
            selectedDay(selectedDay, monthIndex)
        }
    }

    private fun unselectLastSelectedDay() {
        val updatedMonths = state.value.calendarMonths.toMutableList()
        state.value.selectedMonthIndex?.let { lastSelectedMonth ->
            val updatedWeeks = mutableListOf<CalendarWeek>()
            state.value.calendarMonths[lastSelectedMonth].weeks.forEach { week ->
                val days = week.days.map { day ->
                    day.copy(selected = false)
                }
                updatedWeeks.add(CalendarWeek(days))
            }
            updatedMonths[lastSelectedMonth] = state.value.calendarMonths[lastSelectedMonth].copy(
                weeks = updatedWeeks
            )
        }
        _state.value = state.value.copy(calendarMonths = updatedMonths)
    }

    private fun selectedDay(selectedDay: CalendarDay?, monthIndex: Int?) {
        val updatedMonths = state.value.calendarMonths.toMutableList()
        if (selectedDay != null && monthIndex != null) {
            val updatedWeeks = mutableListOf<CalendarWeek>()
            state.value.calendarMonths[monthIndex].weeks.forEach { week ->
                val days = week.days.map { day ->
                    if (selectedDay == day) {
                        day.copy(selected = true)
                    } else {
                        day
                    }
                }
                updatedWeeks.add(CalendarWeek(days))
            }
            updatedMonths[monthIndex] = state.value.calendarMonths[monthIndex].copy(
                weeks = updatedWeeks
            )
        }
        _state.value = state.value.copy(
            selectedDay = selectedDay,
            calendarMonths = updatedMonths,
            selectedMonthIndex = monthIndex
        )
    }

    fun submitAction(action: CalendarAction) {
        viewModelScope.launch {
            pendingActions.emit(action)
        }
    }

    companion object {
        const val AMOUNT_OF_MONTH = 13
        const val NUMBER_OF_DAYS_IN_A_WEEK_PLUS_ONE = 8
        const val NUMBER_OF_DAYS_IN_A_WEEK = 7
    }
}
