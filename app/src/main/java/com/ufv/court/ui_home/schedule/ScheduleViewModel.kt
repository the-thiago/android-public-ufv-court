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
        _state.value = state.value.copy(date = this.date)
        getSchedules()
        handleActions()
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
                    is ScheduleAction.ScheduleClick -> scheduleClick(action.index)
                }
            }
        }
    }

    private fun scheduleClick(indexClick: Int) {
        viewModelScope.launch {
            val newSchedules = state.value.schedules.mapIndexed { index, schedule ->
                if (indexClick == index && !schedule.isScheduled) {
                    if (schedule.selected) {
                        unselect(indexClick = indexClick, schedule = schedule)
                    } else {
                        select(indexClick = indexClick, schedule = schedule)
                    }
                } else {
                    schedule
                }
            }
            _state.value = state.value.copy(schedules = newSchedules)
        }
    }

    private fun unselect(indexClick: Int, schedule: Schedule): Schedule {
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

    private fun select(indexClick: Int, schedule: Schedule): Schedule {
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

    fun submitAction(action: ScheduleAction) {
        viewModelScope.launch {
            pendingActions.emit(action)
        }
    }
}
