package com.ufv.court.ui_home.manage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ufv.court.core.core_common.base.Result
import com.ufv.court.core.core_common.util.ScheduleUtils
import com.ufv.court.core.schedule_service.domain.usecases.GetAllScheduleAfterDateUseCase
import com.ufv.court.core.schedule_service.domain.usecases.GetScheduleByDayUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ManageViewModel @Inject constructor(
    private val getAllScheduleAfterDateUseCase: GetAllScheduleAfterDateUseCase,
    private val getScheduleByDayUseCase: GetScheduleByDayUseCase
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
            val calendar = Calendar.getInstance()
            val result = getAllScheduleAfterDateUseCase(
                GetAllScheduleAfterDateUseCase.Params(
                    timeInMillis = ScheduleUtils.getTimeInMillisFromDate(
                        day = calendar.get(Calendar.DAY_OF_MONTH),
                        month = calendar.get(Calendar.MONTH),
                        year = calendar.get(Calendar.YEAR)
                    )
                )
            )
            if (result is Result.Success) {
                _state.value = state.value.copy(placeholder = false, allSchedules = result.data)
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
                    is ManageAction.ChangeSelectedDate -> {
                        _state.value = state.value.copy(selectedDate = action.date)
                        getSchedulesByDate(action.date)
                    }
                }
            }
        }
    }

    private fun getSchedulesByDate(date: String) {
        viewModelScope.launch {
            if (date.isBlank()) return@launch
            _state.value = state.value.copy(isLoading = true)
            val dateInfo = date.split("/")
            val result = getScheduleByDayUseCase(
                GetScheduleByDayUseCase.Params(
                    timeInMillis = ScheduleUtils.getTimeInMillisFromDate(
                        day = dateInfo[0].toInt(),
                        month = dateInfo[1].toInt(),
                        year = dateInfo[2].toInt(),
                    )
                )
            )
            if (result is Result.Success) {
                _state.value = state.value.copy(schedulesByDate = result.data, isLoading = false)
            } else {
                _state.value = state.value.copy(schedulesByDate = listOf(), isLoading = false)
            }
        }
    }

    fun submitAction(action: ManageAction) {
        viewModelScope.launch {
            pendingActions.emit(action)
        }
    }
}
