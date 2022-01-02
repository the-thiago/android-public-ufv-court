package com.ufv.court.ui_login.forgotpassword

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ufv.court.core.core_common.base.Result
import com.ufv.court.core.user_service.domain.usecase.ResetPasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val resetPasswordUseCase: ResetPasswordUseCase
) : ViewModel() {

    private val pendingActions = MutableSharedFlow<ForgotPasswordAction>()

    private val _state: MutableStateFlow<ForgotPasswordViewState> =
        MutableStateFlow(ForgotPasswordViewState.Empty)
    val state: StateFlow<ForgotPasswordViewState> = _state

    init {
        viewModelScope.launch {
            pendingActions.collect { action ->
                when (action) {
                    ForgotPasswordAction.CleanErrors -> _state.value = state.value.copy(
                        error = null
                    )
                    is ForgotPasswordAction.ChangeEmail -> _state.value = state.value.copy(
                        email = action.email
                    )
                    ForgotPasswordAction.SendEmail -> sendEmail()
                    is ForgotPasswordAction.ChangeShowInfoDialog -> _state.value = state.value.copy(
                        showInfoDialog = action.show
                    )
                    is ForgotPasswordAction.ChangeShowEmailSentDialog -> {
                        _state.value = state.value.copy(showEmailSentDialog = action.show)
                    }
                }
            }
        }
    }

    private fun sendEmail() {
        viewModelScope.launch {
            if (emailIsValid()) {
                _state.value = state.value.copy(isLoading = true)
                val result = resetPasswordUseCase(ResetPasswordUseCase.Params(state.value.email))
                if (result is Result.Success) {
                    _state.value = state.value.copy(isLoading = false, showEmailSentDialog = true)
                } else if (result is Result.Error) {
                    _state.value = state.value.copy(isLoading = false, error = result.exception)
                }
            }
        }
    }

    private fun emailIsValid(): Boolean {
        if (state.value.email.isEmpty() ||
            !Patterns.EMAIL_ADDRESS.matcher(state.value.email).matches() ||
            !state.value.email.endsWith("@ufv.br")
        ) {
            _state.value = state.value.copy(error = ForgotPasswordError.InvalidEmail)
            return false
        }
        return true
    }

    fun submitAction(action: ForgotPasswordAction) {
        viewModelScope.launch {
            pendingActions.emit(action)
        }
    }
}