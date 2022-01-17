package com.ufv.court.ui_myschedule.myschedule

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
class MyScheduleViewModel @Inject constructor(
) : ViewModel() {

    private val pendingActions = MutableSharedFlow<MyScheduleAction>()

    private val _state: MutableStateFlow<MyScheduleViewState> =
        MutableStateFlow(MyScheduleViewState.Empty)
    val state: StateFlow<MyScheduleViewState> = _state

    init {
        viewModelScope.launch {
            pendingActions.collect { action ->
                when (action) {
                    MyScheduleAction.CleanErrors -> _state.value = state.value.copy(error = null)
                }
            }
        }
    }

    fun submitAction(action: MyScheduleAction) {
        viewModelScope.launch {
            pendingActions.emit(action)
        }
    }
}
