package com.ufv.court.ui_home.manage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ufv.court.core.core_common.base.Result
import com.ufv.court.core.core_common.util.ScheduleUtils
import com.ufv.court.core.schedule_service.domain.usecases.GetAllScheduleAfterDateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManageViewModel @Inject constructor(
    private val getAllScheduleAfterDateUseCase: GetAllScheduleAfterDateUseCase
) : ViewModel() {

    private val pendingActions = MutableSharedFlow<ManageAction>()

    private val _state: MutableStateFlow<ManageViewState> =
        MutableStateFlow(ManageViewState.Empty)
    val state: StateFlow<ManageViewState> = _state

    init {
        getAllScheduleAfter()
        handleActions()
    }

    private fun getAllScheduleAfter() {
        viewModelScope.launch {
            val result = getAllScheduleAfterDateUseCase(
                GetAllScheduleAfterDateUseCase.Params(
                    timeInMillis = ScheduleUtils.getTimeInMillisFromDate(
                        day = 8,
                        month = 2,
                        year = 2022
                    )
                )
            )
            if (result is Result.Success) {
                _state.value = state.value.copy(placeholder = false, schedules = result.data)
            } else if (result is Result.Error) {
                _state.value = state.value.copy(error = result.exception)
            }
        }
    }

    private fun handleActions() {
        viewModelScope.launch {
            pendingActions.collect { action ->
                when (action) {
                    ManageAction.CleanErrors -> {
                        _state.value = state.value.copy(error = null)
                    }
                }
            }
        }
    }

    fun submitAction(action: ManageAction) {
        viewModelScope.launch {
            pendingActions.emit(action)
        }
    }
}
