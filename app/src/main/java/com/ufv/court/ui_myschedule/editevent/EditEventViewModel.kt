package com.ufv.court.ui_myschedule.editevent

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ufv.court.core.core_common.base.Result
import com.ufv.court.core.schedule_service.domain.usecases.GetScheduleUseCase
import com.ufv.court.core.schedule_service.domain.usecases.UpdateScheduleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditEventViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    val getScheduleUseCase: GetScheduleUseCase,
    val updateScheduleUseCase: UpdateScheduleUseCase
) : ViewModel() {

    private val pendingActions = MutableSharedFlow<EditEventAction>()

    private val _state: MutableStateFlow<EditEventViewState> =
        MutableStateFlow(EditEventViewState.Empty)
    val state: StateFlow<EditEventViewState> = _state

    private val scheduleId: String = savedStateHandle.get<String>("id") ?: ""

    init {
        handleActions()
        getSchedule()
    }

    private fun getSchedule() {
        viewModelScope.launch {
            val result = getScheduleUseCase(GetScheduleUseCase.Params(id = scheduleId))
            if (result is Result.Success) {
                val schedule = result.data
                val remainingFreeSpaces = schedule.freeSpaces - schedule.filledSpaces
                _state.value = state.value.copy(
                    schedule = schedule,
                    placeholder = false,
                    remainingFreeSpaces = remainingFreeSpaces.toString()
                )
            } else if (result is Result.Error) {
                _state.value = state.value.copy(error = result.exception)
            }
        }
    }

    private fun handleActions() {
        viewModelScope.launch {
            pendingActions.collect { action ->
                when (action) {
                    EditEventAction.CleanErrors -> _state.value = state.value.copy(error = null)
                    is EditEventAction.ChangeTitle -> {
                        state.value.schedule?.let {
                            _state.value = state.value.copy(
                                schedule = it.copy(title = action.title)
                            )
                        }
                    }
                    is EditEventAction.ChangeDescription -> {
                        state.value.schedule?.let {
                            _state.value = state.value.copy(
                                schedule = it.copy(description = action.description)
                            )
                        }
                    }
                    is EditEventAction.ChangeEventType -> {
                        state.value.schedule?.let {
                            _state.value = state.value.copy(
                                schedule = it.copy(scheduleType = action.eventType)
                            )
                        }
                    }
                    is EditEventAction.ChangeFreeSpaces -> {
                        _state.value = state.value.copy(
                            remainingFreeSpaces = action.freeSpaces
                        )
                    }
                    EditEventAction.UpdateEventClick -> updateEvent()
                }
            }
        }
    }

    private fun updateEvent() {
        viewModelScope.launch {
            val schedule = state.value.schedule
            if (validInfo() && schedule != null) {
                _state.value = state.value.copy(isLoading = true)
                val newSchedule = schedule.copy(
                    freeSpaces = schedule.filledSpaces + state.value.remainingFreeSpaces.toInt()
                )
                val result = updateScheduleUseCase(
                    UpdateScheduleUseCase.Params(
                        id = scheduleId,
                        newSchedule = newSchedule
                    )
                )
                if (result is Result.Success) {
                    _state.value = state.value.copy(showSavedDialog = true)
                } else if (result is Result.Error) {
                    _state.value = state.value.copy(error = result.exception)
                }
                _state.value = state.value.copy(isLoading = false)
            }
        }
    }

    private fun validInfo(): Boolean {
        return with(state.value) {
            if (schedule == null) return@with false
            if (schedule.title.isBlank() || schedule.scheduleType.isEmpty() ||
                remainingFreeSpaces.isBlank()
            ) {
                _state.value = state.value.copy(error = EditEventError.EmptyField)
                return@with false
            }
            return@with true
        }
    }

    fun submitAction(action: EditEventAction) {
        viewModelScope.launch {
            pendingActions.emit(action)
        }
    }
}
