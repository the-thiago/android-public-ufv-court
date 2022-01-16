package com.ufv.court.ui_home.schedule

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val pendingActions = MutableSharedFlow<ScheduleAction>()

    private val _state: MutableStateFlow<ScheduleViewState> =
        MutableStateFlow(ScheduleViewState.Empty)
    val state: StateFlow<ScheduleViewState> = _state

    private val date: String = savedStateHandle.get<String>("date") ?: ""

    private var numbOfSelected = 0

    init {
        formatDate()
        getSchedules()
        handleActions()
    }

    private fun formatDate() {
        viewModelScope.launch {
            val dateInfo = date.split("/")
            var formattedDate = ""
            dateInfo.forEach {
                formattedDate += "${it.padStart(2, '0')}/"
            }
            _state.value = state.value.copy(date = formattedDate.dropLast(1))
        }
    }

    private fun getSchedules() {
        viewModelScope.launch {
            val schedules = mutableListOf<Schedule>()
            (7..23).forEach {
                schedules.add(
                    Schedule(
                        hourStart = it,
                        hourEnd = if (it + 1 == 24) 0 else it + 1,
                        isScheduled = false,
                        selected = false
                    )
                )
            }
            _state.value = state.value.copy(schedules = schedules)
        }
    }

    private fun handleActions() {
        viewModelScope.launch {
            pendingActions.collect { action ->
                when (action) {
                    ScheduleAction.CleanErrors -> _state.value = state.value.copy(error = null)
                    is ScheduleAction.ScheduleTimeClick -> scheduleTimeClick(action.index)
                    is ScheduleAction.ChangeScheduleType -> _state.value = state.value.copy(
                        scheduleType = action.type
                    )
                    is ScheduleAction.ChangeTitle -> _state.value = state.value.copy(
                        title = action.title
                    )
                    is ScheduleAction.ChangeDescription -> _state.value = state.value.copy(
                        description = action.description
                    )
                    is ScheduleAction.ChangeHasFreeSpace -> _state.value = state.value.copy(
                        hasFreeSpace = action.has
                    )
                    is ScheduleAction.ChangeFreeSpace -> _state.value = state.value.copy(
                        freeSpaces = action.freeSpace
                    )
                    ScheduleAction.CreateScheduleClick -> createSchedule()
                }
            }
        }
    }

    private fun scheduleTimeClick(indexClick: Int) {
        viewModelScope.launch {
            val newSchedules = state.value.schedules.mapIndexed { index, schedule ->
                if (indexClick == index && !schedule.isScheduled) {
                    if (schedule.selected) {
                        unselectTime(indexClick = indexClick, schedule = schedule)
                    } else {
                        selectTime(indexClick = indexClick, schedule = schedule)
                    }
                } else {
                    schedule
                }
            }
            _state.value = state.value.copy(schedules = newSchedules)
        }
    }

    private fun unselectTime(indexClick: Int, schedule: Schedule): Schedule {
        val schedules = state.value.schedules
        if (numbOfSelected == 1) {
            numbOfSelected--
            return schedule.copy(selected = !schedule.selected)
        } else {
            var previousOrNextScheduleIsUnselected = false
            if (indexClick > 0 && !schedules[indexClick - 1].selected) {
                previousOrNextScheduleIsUnselected = true
            }
            if (indexClick < schedules.lastIndex && !schedules[indexClick + 1].selected) {
                previousOrNextScheduleIsUnselected = true
            }
            if (indexClick == 0 || indexClick == schedules.lastIndex) {
                previousOrNextScheduleIsUnselected = true
            }
            return if (previousOrNextScheduleIsUnselected) {
                numbOfSelected--
                schedule.copy(selected = !schedule.selected)
            } else {
                schedule
            }
        }
    }

    private fun selectTime(indexClick: Int, schedule: Schedule): Schedule {
        val schedules = state.value.schedules
        if (numbOfSelected > 0) {
            var previousOrNextScheduleIsSelected = false
            if (indexClick > 0 && schedules[indexClick - 1].selected) {
                previousOrNextScheduleIsSelected = true
            }
            if (indexClick < schedules.lastIndex && schedules[indexClick + 1].selected) {
                previousOrNextScheduleIsSelected = true
            }
            return if (previousOrNextScheduleIsSelected) {
                numbOfSelected++
                schedule.copy(selected = !schedule.selected)
            } else {
                schedule
            }
        } else {
            numbOfSelected++
            return schedule.copy(selected = !schedule.selected)
        }
    }

    private fun createSchedule() {
        viewModelScope.launch {
            if (validInfo()) {
                // todo schedule backend
                // on success
                _state.value = state.value.copy(showScheduledDialog = true)
            }
        }
    }

    private fun validInfo(): Boolean {
        with(state.value) {
            if (title.isBlank() || scheduleType.isEmpty() || (hasFreeSpace && freeSpaces.isEmpty())) {
                _state.value = state.value.copy(error = ScheduleError.EmptyField)
                return@with false
            }
            val selectedTimes = schedules.filter { it.selected }
            if (selectedTimes.isEmpty()) {
                _state.value = state.value.copy(error = ScheduleError.UnselectTimeField)
                return@with false
            }
            return@with true
        }
        return true
    }

    fun submitAction(action: ScheduleAction) {
        viewModelScope.launch {
            pendingActions.emit(action)
        }
    }
}