package com.ufv.court.ui_login.register

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ufv.court.core.core_common.base.Result
import com.ufv.court.core.user_service.domain.usecase.LogoutUserUseCase
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
class RegisterViewModel @Inject constructor(
    private val registerUserUseCase: RegisterUserUseCase,
    private val sendEmailVerificationUseCase: SendEmailVerificationUseCase,
    private val logoutUserUseCase: LogoutUserUseCase
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
                    RegisterAction.CleanErrors -> {
                        _state.value = state.value.copy(error = null)
                    }
                    is RegisterAction.ChangeName -> _state.value = state.value.copy(
                        name = action.name
                    )
                    is RegisterAction.ChangeImageUri -> _state.value = state.value.copy(
                        imageUri = action.uri
                    )
                    is RegisterAction.ChangeEmail -> _state.value = state.value.copy(
                        email = action.email
                    )
                    is RegisterAction.ChangePassword -> _state.value = state.value.copy(
                        password = action.password
                    )
                    is RegisterAction.ChangeConfirmPassword -> {
                        _state.value = state.value.copy(confirmPassword = action.confirmPassword)
                    }
                    RegisterAction.RegisterCredentials -> register()
                    is RegisterAction.ShowEmailSentDialog -> {
                        _state.value = state.value.copy(showEmailSentDialog = action.show)
                    }
                }
            }
        }
    }

    private fun register() {
        viewModelScope.launch {
            if (areValidInformation()) {
                _state.value = state.value.copy(isLoading = true)
                val result = registerUserUseCase(
                    parameters = RegisterUserUseCase.Params(
                        email = state.value.email,
                        password = state.value.password,
                        name = state.value.name
                    )
                )
                if (result is Result.Success) {
                    sendEmailVerification()
                } else if (result is Result.Error) {
                    _state.value = state.value.copy(isLoading = false, error = result.exception)
                }
                logoutUserUseCase(Unit)
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

    private fun areValidInformation(): Boolean {
        if (state.value.email.isBlank() ||
            state.value.password.isBlank() ||
            state.value.confirmPassword.isBlank() ||
            state.value.name.isBlank()
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

    fun submitAction(credentialsAction: RegisterAction) {
        viewModelScope.launch {
            pendingActions.emit(credentialsAction)
        }
    }
}