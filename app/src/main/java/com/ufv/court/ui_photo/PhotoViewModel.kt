package com.ufv.court.ui_photo

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class PhotoViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _state: MutableStateFlow<PhotoViewState> = MutableStateFlow(PhotoViewState.Empty)
    val state: StateFlow<PhotoViewState> = _state

    private val image = savedStateHandle.get<String>("url") ?: ""

    init {
        _state.value = state.value.copy(image = image)
    }
}
