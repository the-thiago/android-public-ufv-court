package com.ufv.court.ui_login.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {

    private val pendingActions = MutableSharedFlow<RegisterAction>()

    private val _state: MutableStateFlow<RegisterViewState> = MutableStateFlow(
        RegisterViewState.Empty
    )
    val state: StateFlow<RegisterViewState> = _state

    init {
        viewModelScope.launch {
            pendingActions.collect { action ->
                when (action) {
                    is RegisterAction.ChangeEmailText -> _state.value = state.value.copy(
                        email = action.email
                    )
                    is RegisterAction.ChangePasswordText -> _state.value = state.value.copy(
                        password = action.password
                    )
                }
            }
        }
    }

    fun submitAction(action: RegisterAction) {
        viewModelScope.launch {
            pendingActions.emit(action)
        }
    }
}