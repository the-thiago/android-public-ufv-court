package com.ufv.court.ui_login.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val pendingActions = MutableSharedFlow<LoginAction>()

    private val _state: MutableStateFlow<LoginViewState> = MutableStateFlow(LoginViewState.Empty)
    val state: StateFlow<LoginViewState> = _state

    init {
        viewModelScope.launch {
            pendingActions.collect { action ->
                when (action) {
                    is LoginAction.ChangeEmailText -> _state.value = state.value.copy(
                        email = action.email
                    )
                    is LoginAction.ChangePasswordText -> _state.value = state.value.copy(
                        password = action.password
                    )
                    LoginAction.ChangePasswordVisibility -> _state.value = state.value.copy(
                        isPasswordVisible = !state.value.isPasswordVisible
                    )
                    LoginAction.Login -> doLogin()
                }
            }
        }
    }

    private fun doLogin() {
        viewModelScope.launch {
            _state.value = state.value.copy(isLoading = true)
            delay(500L)
            _state.value = state.value.copy(isLoading = false)
        }
    }

    fun submitAction(action: LoginAction) {
        viewModelScope.launch {
            pendingActions.emit(action)
        }
    }
}