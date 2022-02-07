package com.ufv.court.ui_myschedule.participants

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ufv.court.core.core_common.base.Result
import com.ufv.court.core.schedule_service.domain.usecases.GetScheduleUseCase
import com.ufv.court.core.user_service.domain.usecase.GetUsersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ParticipantsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getScheduleUseCase: GetScheduleUseCase,
    private val getUsersUseCase: GetUsersUseCase
) : ViewModel() {

    private val _state: MutableStateFlow<ParticipantsViewState> =
        MutableStateFlow(ParticipantsViewState.Empty)
    val state: StateFlow<ParticipantsViewState> = _state

    private val scheduleId: String = savedStateHandle.get<String>("eventId") ?: ""

    init {
        getSchedule()
    }

    private fun getSchedule() {
        viewModelScope.launch {
            val result = getScheduleUseCase(GetScheduleUseCase.Params(id = scheduleId))
            if (result is Result.Success) {
                getParticipants(ids = result.data.participantsId)
            } else if (result is Result.Error) {
                _state.value = state.value.copy(error = result.exception)
            }
        }
    }

    private suspend fun getParticipants(ids: List<String>) {
        val result = getUsersUseCase(GetUsersUseCase.Params(ids = ids))
        if (result is Result.Success) {
            _state.value = state.value.copy(participants = result.data)
        } else if (result is Result.Error) {
            _state.value = state.value.copy(error = result.exception)
        }
    }
}
