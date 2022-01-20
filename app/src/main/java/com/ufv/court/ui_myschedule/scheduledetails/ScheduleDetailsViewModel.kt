package com.ufv.court.ui_myschedule.scheduledetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ufv.court.core.core_common.base.Result
import com.ufv.court.core.schedule_service.domain.usecases.GetScheduleUseCase
import com.ufv.court.core.schedule_service.domain.usecases.UpdateScheduleUseCase
import com.ufv.court.core.user_service.domain.usecase.GetCurrentUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScheduleDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getScheduleUseCase: GetScheduleUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val updateScheduleUseCase: UpdateScheduleUseCase
) : ViewModel() {

    private val pendingActions = MutableSharedFlow<ScheduleDetailsAction>()

    private val _state: MutableStateFlow<ScheduleDetailsViewState> =
        MutableStateFlow(ScheduleDetailsViewState.Empty)
    val state: StateFlow<ScheduleDetailsViewState> = _state

    private val scheduleId: String = savedStateHandle.get<String>("id") ?: ""

    init {
        _state.value = state.value.copy(scheduleId = scheduleId)
        handleActions()
    }

    private fun getScheduleAndUser() {
        viewModelScope.launch {
            val scheduleResult = getScheduleUseCase(GetScheduleUseCase.Params(id = scheduleId))
            if (scheduleResult is Result.Success) {
                _state.value = state.value.copy(schedule = scheduleResult.data)
            } else if (scheduleResult is Result.Error) {
                _state.value = state.value.copy(error = scheduleResult.exception)
            }
            val userResult = getCurrentUserUseCase(Unit)
            if (userResult is Result.Success) {
                _state.value = state.value.copy(user = userResult.data)
            } else if (userResult is Result.Error) {
                _state.value = state.value.copy(error = userResult.exception)
            }
            if (scheduleResult is Result.Success && userResult is Result.Success) {
                _state.value = state.value.copy(
                    isTheOwner = scheduleResult.data.ownerId == userResult.data.id
                )
            }
            _state.value = state.value.copy(placeholder = false)
        }
    }

    private fun handleActions() {
        viewModelScope.launch {
            pendingActions.collect { action ->
                when (action) {
                    ScheduleDetailsAction.CleanErrors -> {
                        _state.value = state.value.copy(error = null)
                    }
                    is ScheduleDetailsAction.ChangeShowCancellationDialog -> {
                        _state.value = state.value.copy(showCancellationDialog = action.show)
                    }
                    ScheduleDetailsAction.ConfirmEventCancellation -> cancelEvent()
                    is ScheduleDetailsAction.ChangeShowCancelledDialog -> {
                        _state.value = state.value.copy(showCancelledDialog = action.show)
                    }
                    ScheduleDetailsAction.ReloadData -> getScheduleAndUser()
                }
            }
        }
    }

    private fun cancelEvent() {
        viewModelScope.launch {
            val schedule = state.value.schedule ?: return@launch
            _state.value = state.value.copy(showCancellationDialog = false)
            val newSchedule = schedule.copy(cancelled = true)
            val result = updateScheduleUseCase(
                UpdateScheduleUseCase.Params(
                    id = schedule.id,
                    newSchedule = newSchedule
                )
            )
            if (result is Result.Success) {
                _state.value = state.value.copy(showCancelledDialog = true, schedule = newSchedule)
            } else if (result is Result.Error) {
                _state.value = state.value.copy(error = result.exception)
            }
        }
    }

    fun submitAction(action: ScheduleDetailsAction) {
        viewModelScope.launch {
            pendingActions.emit(action)
        }
    }
}
