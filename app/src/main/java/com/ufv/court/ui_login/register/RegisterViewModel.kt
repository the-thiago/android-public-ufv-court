package com.ufv.court.ui_login.register

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ufv.court.core.core_common.base.Result
import com.ufv.court.core.user_service.domain.usecase.RegisterUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUserUseCase: RegisterUserUseCase
) : ViewModel() {

    private val pendingActions = MutableSharedFlow<RegisterAction>()

    private val _state: MutableStateFlow<RegisterViewState> = MutableStateFlow(
        RegisterViewState.Empty
    )
    val state: StateFlow<RegisterViewState> = _state

    init {
        viewModelScope.launch {
            pendingActions.collect { action ->
                when (action) {
                    RegisterAction.CleanErrors -> _state.value = state.value.copy(error = null)
                    RegisterAction.NavigateUp -> {
                        // todo
                    }
                    is RegisterAction.ChangeEmail -> _state.value = state.value.copy(
                        email = action.email
                    )
                    is RegisterAction.ChangePassword -> _state.value = state.value.copy(
                        password = action.password
                    )
                    is RegisterAction.ChangeConfirmPassword -> _state.value = state.value.copy(
                        confirmPassword = action.confirmPassword
                    )
                    RegisterAction.Register -> register()
                }
            }
        }
    }

    private fun register() {
        viewModelScope.launch {
            if (areValidCredentials()) {
                _state.value = state.value.copy(isLoading = true)
                val result = registerUserUseCase(
                    parameters = RegisterUserUseCase.Params(
                        email = state.value.email,
                        password = state.value.password
                    )
                )
                if (result is Result.Success) {
                    _state.value = state.value.copy(isLoading = false)
                    // todo navigate to home
                } else if (result is Result.Error) {
                    _state.value = state.value.copy(isLoading = false, error = result.exception)
                }
            }
        }
    }

    private fun areValidCredentials(): Boolean {
        if (state.value.email.isBlank() ||
            state.value.password.isBlank() ||
            state.value.confirmPassword.isBlank()
        ) {
            _state.value = state.value.copy(error = RegisterError.EmptyField)
            return false
        } else if (state.value.password != state.value.confirmPassword) {
            _state.value = state.value.copy(error = RegisterError.DifferentPassword)
            return false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(state.value.email).matches() ||
            !state.value.email.endsWith("@ufv.br")
        ) {
            _state.value = state.value.copy(error = RegisterError.EmailDomainNotAllowed)
            return false
        }
        return true
    }

    fun submitAction(action: RegisterAction) {
        viewModelScope.launch {
            pendingActions.emit(action)
        }
    }
}