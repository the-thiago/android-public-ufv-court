package com.ufv.court.ui_login.registercredentials

import android.util.Patterns
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ufv.court.core.core_common.base.Result
import com.ufv.court.core.user_service.domain.usecase.RegisterUserUseCase
import com.ufv.court.core.user_service.domain.usecase.SendEmailVerificationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterCredentialsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val registerUserUseCase: RegisterUserUseCase,
    private val sendEmailVerificationUseCase: SendEmailVerificationUseCase
) : ViewModel() {

    private val pendingActions = MutableSharedFlow<RegisterCredentialsAction>()

    private val _state: MutableStateFlow<RegisterCredentialsViewState> = MutableStateFlow(
        RegisterCredentialsViewState.Empty
    )
    val state: StateFlow<RegisterCredentialsViewState> = _state

    private val name = savedStateHandle.get<String>("name") ?: ""

    init {
        viewModelScope.launch {
            pendingActions.collect { action ->
                when (action) {
                    RegisterCredentialsAction.CleanErrors -> {
                        _state.value = state.value.copy(error = null)
                    }
                    is RegisterCredentialsAction.ChangeEmail -> _state.value = state.value.copy(
                        email = action.email
                    )
                    is RegisterCredentialsAction.ChangePassword -> _state.value = state.value.copy(
                        password = action.password
                    )
                    is RegisterCredentialsAction.ChangeConfirmPassword -> {
                        _state.value = state.value.copy(confirmPassword = action.confirmPassword)
                    }
                    RegisterCredentialsAction.RegisterCredentials -> register()
                    is RegisterCredentialsAction.ShowEmailSentDialog -> {
                        _state.value = state.value.copy(showEmailSentDialog = action.show)
                    }
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
                        password = state.value.password,
                        name = name
                    )
                )
                if (result is Result.Success) {
                    sendEmailVerification()
                } else if (result is Result.Error) {
                    _state.value = state.value.copy(isLoading = false, error = result.exception)
                }
            }
        }
    }

    private suspend fun sendEmailVerification() {
        val result = sendEmailVerificationUseCase(Unit)
        if (result is Result.Success) {
            _state.value = state.value.copy(isLoading = false, showEmailSentDialog = true)
        } else if (result is Result.Error) {
            _state.value = state.value.copy(isLoading = false, error = result.exception)
        }
    }

    private fun areValidCredentials(): Boolean {
        if (state.value.email.isBlank() ||
            state.value.password.isBlank() ||
            state.value.confirmPassword.isBlank()
        ) {
            _state.value = state.value.copy(error = RegisterCredentialsError.EmptyField)
            return false
        } else if (state.value.password != state.value.confirmPassword) {
            _state.value = state.value.copy(error = RegisterCredentialsError.DifferentPassword)
            return false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(state.value.email).matches() ||
            !state.value.email.endsWith("@ufv.br")
        ) {
            _state.value = state.value.copy(error = RegisterCredentialsError.EmailDomainNotAllowed)
            return false
        }
        return true
    }

    fun submitAction(credentialsAction: RegisterCredentialsAction) {
        viewModelScope.launch {
            pendingActions.emit(credentialsAction)
        }
    }
}