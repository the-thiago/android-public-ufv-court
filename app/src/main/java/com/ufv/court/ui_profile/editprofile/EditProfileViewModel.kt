package com.ufv.court.ui_profile.editprofile

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
class EditProfileViewModel @Inject constructor(

) : ViewModel() {

    private val pendingActions = MutableSharedFlow<EditProfileAction>()

    private val _state: MutableStateFlow<EditProfileViewState> =
        MutableStateFlow(EditProfileViewState.Empty)
    val state: StateFlow<EditProfileViewState> = _state

    init {
        viewModelScope.launch {
            pendingActions.collect { action ->
                when (action) {
                    EditProfileAction.CleanErrors -> _state.value = state.value.copy(error = null)
                }
            }
        }
    }

    fun submitAction(action: EditProfileAction) {
        viewModelScope.launch {
            pendingActions.emit(action)
        }
    }
}
