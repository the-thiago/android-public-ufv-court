package com.ufv.court.ui_login.registeruser

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterUserViewModel @Inject constructor() : ViewModel() {

    private val pendingActions = MutableSharedFlow<RegisterUserAction>()

    private val _state: MutableStateFlow<RegisterUserViewState> = MutableStateFlow(
        RegisterUserViewState.Empty
    )
    val state: StateFlow<RegisterUserViewState> = _state

    init {
        viewModelScope.launch {
            pendingActions.collect { action ->
                when (action) {
                    RegisterUserAction.CleanErrors -> _state.value = state.value.copy(
                        error = null
                    )
                    is RegisterUserAction.ChangeName -> _state.value = state.value.copy(
                        name = action.name
                    )
                    is RegisterUserAction.ContinueClick -> openRegisterCredentials(action.onSuccess)
                    is RegisterUserAction.ChangeImageUri -> _state.value = state.value.copy(
                        imageUri = action.uri
                    )
                }
            }
        }
    }

    private fun openRegisterCredentials(onSuccess: (String) -> Unit) {
        viewModelScope.launch {
            val name = state.value.name.trim()
            if (name.isBlank()) {
                _state.value = state.value.copy(error = RegisterUserError.EmptyName)
            } else {
                onSuccess(name)
            }
        }
    }

    fun submitAction(credentialsAction: RegisterUserAction) {
        viewModelScope.launch {
            pendingActions.emit(credentialsAction)
        }
    }
}