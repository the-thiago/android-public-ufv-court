package com.ufv.court.ui_profile.editprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ufv.court.core.core_common.base.Result
import com.ufv.court.core.user_service.domain.model.UserModel
import com.ufv.court.core.user_service.domain.usecase.GetCurrentUserUseCase
import com.ufv.court.core.user_service.domain.usecase.UpdateUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val updateUserUseCase: UpdateUserUseCase
) : ViewModel() {

    companion object {
        const val PHONE_MASK = "(##) #####-####"
        val PHONE_LENGTH = PHONE_MASK.count { it == '#' }
    }

    private val pendingActions = MutableSharedFlow<EditProfileAction>()

    private val _state: MutableStateFlow<EditProfileViewState> =
        MutableStateFlow(EditProfileViewState.Empty)
    val state: StateFlow<EditProfileViewState> = _state

    private var oldUser: UserModel? = null

    init {
        getCurrentUser()
        handleActions()
    }

    private fun handleActions() {
        viewModelScope.launch {
            pendingActions.collect { action ->
                when (action) {
                    EditProfileAction.CleanErrors -> _state.value = state.value.copy(error = null)
                    is EditProfileAction.ChangeImageUri -> {
                        _state.value = state.value.copy(newImageUri = action.uri)
                    }
                    is EditProfileAction.ChangeName -> {
                        _state.value = state.value.copy(name = action.name)
                    }
                    EditProfileAction.SaveProfile -> saveProfile()
                    is EditProfileAction.ChangePhone -> {
                        _state.value = state.value.copy(phone = action.phone)
                    }
                }
            }
        }
    }

    private fun saveProfile() {
        viewModelScope.launch {
            if (validInfo()) {
                _state.value = state.value.copy(isLoading = true)
                val oldUser = oldUser ?: return@launch
                val result = updateUserUseCase(
                    UpdateUserUseCase.Params(
                        user = oldUser.copy(name = state.value.name, phone = state.value.phone),
                        imageUri = state.value.newImageUri
                    )
                )
                if (result is Result.Success) {
                    _state.value = state.value.copy(isLoading = false, profileEditedDialog = true)
                } else if (result is Result.Error) {
                    _state.value = state.value.copy(isLoading = false, error = result.exception)
                }
            }
        }
    }

    private fun validInfo(): Boolean {
        return with(state.value) {
            if (name.isBlank()) {
                _state.value = state.value.copy(error = EditProfileError.EmptyField)
                return@with false
            }
            if (phone.isNotBlank() && phone.length != 11) {
                _state.value = state.value.copy(error = EditProfileError.InvalidPhone)
                return@with false
            }
            return@with true
        }
    }

    private fun getCurrentUser() {
        viewModelScope.launch {
            val result = getCurrentUserUseCase(Unit)
            if (result is Result.Success) {
                oldUser = result.data
                _state.value = state.value.copy(
                    name = result.data.name,
                    phone = result.data.phone,
                    currentImage = result.data.image,
                    placeholder = false
                )
            } else if (result is Result.Error) {
                _state.value = state.value.copy(error = result.exception)
            }
        }
    }

    fun submitAction(action: EditProfileAction) {
        viewModelScope.launch {
            pendingActions.emit(action)
        }
    }
}
