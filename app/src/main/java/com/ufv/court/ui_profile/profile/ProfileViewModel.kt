package com.ufv.court.ui_profile.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ufv.court.core.core_common.base.Result
import com.ufv.court.core.user_service.domain.usecase.GetCurrentUserUseCase
import com.ufv.court.core.user_service.domain.usecase.LogoutUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val logoutUserUseCase: LogoutUserUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    private val pendingActions = MutableSharedFlow<ProfileAction>()

    private val _state: MutableStateFlow<ProfileViewState> =
        MutableStateFlow(ProfileViewState.Empty)
    val state: StateFlow<ProfileViewState> = _state

    init {
        getCurrentUser()
        handleActions()
    }

    private fun handleActions() {
        viewModelScope.launch {
            pendingActions.collect { action ->
                when (action) {
                    ProfileAction.CleanErrors -> _state.value = state.value.copy(error = null)
                    is ProfileAction.ChangeShowConfirmLogoutDialog -> {
                        _state.value = state.value.copy(showConfirmLogoutDialog = action.show)
                    }
                    is ProfileAction.ConfirmLogout -> logout(action.onSuccess)
                    ProfileAction.Refresh -> {
                        _state.value = state.value.copy(isRefreshing = true)
                        getCurrentUser()
                    }
                }
            }
        }
    }

    private fun getCurrentUser() {
        viewModelScope.launch {
            val result = getCurrentUserUseCase(Unit)
            delay(2000L)
            if (result is Result.Success) {
                _state.value = state.value.copy(
                    email = result.data.email,
                    name = result.data.name,
                    phone = result.data.phone,
                    image = result.data.image,
                    placeholder = false,
                    isRefreshing = false
                )
            }
        }
    }

    private fun logout(onSuccess: () -> Unit) {
        viewModelScope.launch {
            val result = logoutUserUseCase(Unit)
            _state.value = state.value.copy(showConfirmLogoutDialog = false)
            if (result is Result.Success) {
                onSuccess()
            } else if (result is Result.Error) {
                _state.value = state.value.copy(error = ProfileError.LogoutError)
            }
        }
    }

    fun submitAction(action: ProfileAction) {
        viewModelScope.launch {
            pendingActions.emit(action)
        }
    }
}