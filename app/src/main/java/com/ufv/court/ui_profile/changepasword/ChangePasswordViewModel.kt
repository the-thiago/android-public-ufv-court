package com.ufv.court.ui_profile.changepasword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChangePasswordViewModel @Inject constructor(

) : ViewModel() {

    private val pendingActions = MutableSharedFlow<ChangePasswordAction>()

    private val _state: MutableStateFlow<ChangePasswordViewState> =
        MutableStateFlow(ChangePasswordViewState.Empty)
    val state: StateFlow<ChangePasswordViewState> = _state

    init {
        viewModelScope.launch {
            pendingActions.collect { action ->
                when (action) {
                    ChangePasswordAction.CleanErrors -> _state.value = state.value.copy(
                        error = null
                    )
                }
            }
        }
    }

    fun submitAction(action: ChangePasswordAction) {
        viewModelScope.launch {
            pendingActions.emit(action)
        }
    }
}
