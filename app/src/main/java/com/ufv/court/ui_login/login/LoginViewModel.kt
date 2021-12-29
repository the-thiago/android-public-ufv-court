package com.ufv.court.ui_login.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ufv.court.core.core_common.base.Result
import com.ufv.court.core.user_service.domain.usecase.IsEmailVerifiedUseCase
import com.ufv.court.core.user_service.domain.usecase.LoginUseCase
import com.ufv.court.core.user_service.domain.usecase.SendEmailVerificationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val isEmailVerifiedUseCase: IsEmailVerifiedUseCase,
    private val sendEmailVerificationUseCase: SendEmailVerificationUseCase
) : ViewModel() {

    private val pendingActions = MutableSharedFlow<LoginAction>()

    private val _state: MutableStateFlow<LoginViewState> = MutableStateFlow(LoginViewState.Empty)
    val state: StateFlow<LoginViewState> = _state

    private var emailSent = false

    init {
        viewModelScope.launch {
            pendingActions.collect { action ->
                when (action) {
                    LoginAction.CleanErrors -> _state.value = state.value.copy(error = null)
                    is LoginAction.ChangeEmailText -> _state.value = state.value.copy(
                        email = action.email
                    )
                    is LoginAction.ChangePasswordText -> _state.value = state.value.copy(
                        password = action.password
                    )
                    LoginAction.ChangePasswordVisibility -> _state.value = state.value.copy(
                        isPasswordVisible = !state.value.isPasswordVisible
                    )
                    is LoginAction.Login -> doLogin(action.onSuccess)
                    LoginAction.ForgotPasswordClick -> {
                        // todo navigate
                    }
                }
            }
        }
    }

    private fun doLogin(onSuccess: () -> Unit) {
        viewModelScope.launch {
            if (invalidCredentials()) return@launch
            _state.value = state.value.copy(isLoading = true)
            val result = loginUseCase(
                LoginUseCase.Params(email = state.value.email, password = state.value.password)
            )
            if (result is Result.Success) {
                verifyIfEmailIsVerified(onSuccess)
            } else if (result is Result.Error) {
                _state.value = state.value.copy(error = result.exception)
            }
            _state.value = state.value.copy(isLoading = false)
        }
    }

    private fun invalidCredentials(): Boolean {
        if (state.value.email.isEmpty() || state.value.password.isEmpty()) {
            _state.value = state.value.copy(error = LoginError.EmptyField)
            return true
        }
        return false
    }

    private suspend fun verifyIfEmailIsVerified(onSuccess: () -> Unit) {
        val result = isEmailVerifiedUseCase(Unit)
        if (result is Result.Success) {
            val verified = result.data
            if (verified) {
                onSuccess()
            } else {
                sendVerificationEmail()
            }
        } else if (result is Result.Error) {
            sendVerificationEmail()
        }
    }

    private suspend fun sendVerificationEmail() {
        _state.value = state.value.copy(error = LoginError.EmailNotVerified)
        if (!emailSent) {
            emailSent = true
            sendEmailVerificationUseCase(Unit)
        }
    }

    fun submitAction(action: LoginAction) {
        viewModelScope.launch {
            pendingActions.emit(action)
        }
    }
}