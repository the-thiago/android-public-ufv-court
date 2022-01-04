package com.ufv.court.ui_profile.changepasword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ufv.court.core.core_common.base.Result
import com.ufv.court.core.user_service.domain.usecase.ChangePasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val changePasswordUseCase: ChangePasswordUseCase
) : ViewModel() {

    private val pendingActions = MutableSharedFlow<ChangePasswordAction>()

    private val _state: MutableStateFlow<ChangePasswordViewState> =
        MutableStateFlow(ChangePasswordViewState.Empty)
    val state: StateFlow<ChangePasswordViewState> = _state

    init {
        viewModelScope.launch {
            pendingActions.collect { action ->
                when (action) {
                    ChangePasswordAction.CleanErrors -> _state.value =
                        state.value.copy(error = null)
                    is ChangePasswordAction.ChangeShowPasswordChangedDialog -> {
                        _state.value = state.value.copy(showPasswordChangedDialog = action.show)
                    }
                    is ChangePasswordAction.ChangeConfirmPassword -> _state.value =
                        state.value.copy(confirmPassword = action.confirmPassword)
                    is ChangePasswordAction.ChangeCurrentPassword -> _state.value =
                        state.value.copy(currentPassword = action.currentPassword)
                    is ChangePasswordAction.ChangeNewPassword -> _state.value =
                        state.value.copy(newPassword = action.newPassword)
                    ChangePasswordAction.ChangePasswordClick -> changePassword()
                }
            }
        }
    }

    private fun changePassword() {
        viewModelScope.launch {
            if (areValidPasswords()) {
                _state.value = state.value.copy(isLoading = true)
                val result = changePasswordUseCase(
                    parameters = ChangePasswordUseCase.Params(
                        oldPassword = state.value.currentPassword,
                        newPassword = state.value.newPassword
                    )
                )
                if (result is Result.Success) {
                    _state.value = state.value.copy(
                        isLoading = false, showPasswordChangedDialog = true
                    )
                } else if (result is Result.Error) {
                    _state.value = state.value.copy(isLoading = false, error = result.exception)
                }
            }
        }
    }

    private fun areValidPasswords(): Boolean {
        if (state.value.currentPassword.isBlank() ||
            state.value.newPassword.isBlank() ||
            state.value.confirmPassword.isBlank()
        ) {
            _state.value = state.value.copy(error = ChangePasswordError.EmptyField)
            return false
        } else if (state.value.newPassword != state.value.confirmPassword) {
            _state.value = state.value.copy(error = ChangePasswordError.DifferentPasswords)
            return false
        } else if (state.value.newPassword.length < 6) {
            _state.value = state.value.copy(error = ChangePasswordError.AuthWeakPassword)
            return false
        }
        return true
    }

    fun submitAction(action: ChangePasswordAction) {
        viewModelScope.launch {
            pendingActions.emit(action)
        }
    }
}
