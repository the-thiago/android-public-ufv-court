package com.ufv.court.ui_myschedule.scheduledetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ufv.court.core.core_common.base.Result
import com.ufv.court.core.schedule_service.domain.usecases.GetScheduleUseCase
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
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    private val pendingActions = MutableSharedFlow<ScheduleDetailsAction>()

    private val _state: MutableStateFlow<ScheduleDetailsViewState> =
        MutableStateFlow(ScheduleDetailsViewState.Empty)
    val state: StateFlow<ScheduleDetailsViewState> = _state

    private val scheduleId: String = savedStateHandle.get<String>("id") ?: ""

    init {
        getSchedule()
        getCurrentUser()
        handleActions()
    }

    private fun getCurrentUser() {
        viewModelScope.launch {
            val result = getCurrentUserUseCase(Unit)
            if (result is Result.Success) {
                _state.value = state.value.copy(user = result.data)
            } else if (result is Result.Error) {
                _state.value = state.value.copy(error = result.exception)
            }
        }
    }

    private fun getSchedule() {
        viewModelScope.launch {
            val result = getScheduleUseCase(GetScheduleUseCase.Params(id = scheduleId))
            if (result is Result.Success) {
                _state.value = state.value.copy(schedule = result.data)
            } else if (result is Result.Error) {
                _state.value = state.value.copy(error = result.exception)
            }
        }
    }

    private fun handleActions() {
        viewModelScope.launch {
            pendingActions.collect { action ->
                when (action) {
                    ScheduleDetailsAction.CleanErrors -> {
                        _state.value = state.value.copy(error = null)
                    }
                }
            }
        }
    }

    fun submitAction(action: ScheduleDetailsAction) {
        viewModelScope.launch {
            pendingActions.emit(action)
        }
    }
}
