package com.ufv.court.ui_home.schedule

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.ufv.court.core.comments_service.domain.model.ScheduleComments
import com.ufv.court.core.comments_service.domain.usecases.CreateEventCommentsUseCase
import com.ufv.court.core.core_common.base.Result
import com.ufv.court.core.core_common.util.ScheduleUtils
import com.ufv.court.core.schedule_service.domain.model.ScheduleModel
import com.ufv.court.core.schedule_service.domain.usecases.CreateScheduleUseCase
import com.ufv.court.core.schedule_service.domain.usecases.GetScheduleByDayUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val createScheduleUseCase: CreateScheduleUseCase,
    private val getScheduleByDayUseCase: GetScheduleByDayUseCase,
    private val createEventCommentsUseCase: CreateEventCommentsUseCase
) : ViewModel() {

    private val pendingActions = MutableSharedFlow<ScheduleAction>()

    private val _state: MutableStateFlow<ScheduleViewState> =
        MutableStateFlow(ScheduleViewState.Empty)
    val state: StateFlow<ScheduleViewState> = _state

    private val date: String = savedStateHandle.get<String>("date") ?: ""

    private var numbOfSelected = 0

    init {
        getAlreadySchedulesTimes()
        handleActions()
    }

    private fun getAlreadySchedulesTimes() {
        viewModelScope.launch {
            var dateInfo = date.split("/")
            var formattedDate = ""
            dateInfo = dateInfo.map { it.padStart(2, '0') }
            dateInfo.forEach {
                formattedDate += "$it/"
            }
            _state.value = state.value.copy(date = formattedDate.dropLast(1))
            val result = getScheduleByDayUseCase(
                GetScheduleByDayUseCase.Params(
                    timeInMillis = ScheduleUtils.getTimeInMillisFromDate(
                        day = dateInfo[0].toInt(),
                        month = dateInfo[1].toInt(),
                        year = dateInfo[2].toInt()
                    )
                )
            )
            if (result is Result.Success) {
                var schedules = getPossibleSchedulesTimes()
                val notCancelledEvents = result.data.filter { !it.cancelled }
                schedules = schedules.map { it ->
                    for (scheduled in notCancelledEvents) {
                        val itHourEnd = if (it.hourEnd == 0) 24 else it.hourEnd
                        val scheduledHourEnd = if (scheduled.hourEnd == 0) 24 else scheduled.hourEnd
                        if (it.hourStart >= scheduled.hourStart && itHourEnd <= scheduledHourEnd) {
                            return@map it.copy(isScheduled = true)
                        }
                    }
                    it
                }
                _state.value = state.value.copy(schedules = schedules, placeholder = false)
            } else if (result is Result.Error) {
                _state.value = state.value.copy(error = result.exception, placeholder = false)
            }
        }
    }

    private fun getPossibleSchedulesTimes(): List<Schedule> {
        return (7..23).map {
            Schedule(
                hourStart = it,
                hourEnd = if (it + 1 == 24) 0 else it + 1,
                isScheduled = false,
                selected = false
            )
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
                _state.value = state.value.copy(isLoading = true)
                val splitDate = state.value.date.split("/")
                val selectedTimes = state.value.schedules.filter { it.selected }
                val freeSpace = getFreeSpaces()
                val newTime = ScheduleModel(
                    ownerId = FirebaseAuth.getInstance().currentUser?.uid ?: "",
                    hourStart = selectedTimes[0].hourStart,
                    hourEnd = selectedTimes[selectedTimes.lastIndex].hourEnd,
                    title = state.value.title,
                    description = state.value.description,
                    scheduleType = state.value.scheduleType,
                    freeSpaces = freeSpace,
                    timeInMillis = ScheduleUtils.getTimeInMillisFromDate(
                        day = splitDate[0].toInt(),
                        month = splitDate[1].toInt(),
                        year = splitDate[2].toInt()
                    ),
                    hasFreeSpace = freeSpace != 0
                )
                val result = createScheduleUseCase(CreateScheduleUseCase.Params(newTime))
                if (result is Result.Success) {
                    createEventComments(eventId = result.data)
                } else if (result is Result.Error) {
                    _state.value = state.value.copy(error = result.exception, isLoading = false)
                }
            }
        }
    }

    private suspend fun createEventComments(eventId: String) {
        val comments = ScheduleComments(eventId = eventId)
        val result = createEventCommentsUseCase(
            CreateEventCommentsUseCase.Params(eventComments = comments)
        )
        if (result is Result.Success) {
            _state.value = state.value.copy(showScheduledDialog = true, isLoading = false)
        } else if (result is Result.Error) {
            _state.value = state.value.copy(error = result.exception, isLoading = false)
        }
    }

    private fun getFreeSpaces(): Int {
        if (!state.value.hasFreeSpace) return 0
        return state.value.freeSpaces.toInt()
    }

    private fun validInfo(): Boolean {
        return with(state.value) {
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
    }

    fun submitAction(action: ScheduleAction) {
        viewModelScope.launch {
            pendingActions.emit(action)
        }
    }
}
