package com.ufv.court.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ufv.court.core.core_common.base.Result
import com.ufv.court.core.navigation.Screen
import com.ufv.court.core.user_service.domain.usecase.GetCurrentUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    private val _initialDestination = MutableStateFlow(Screen.Login.route)
    val initialDestination: StateFlow<String> = _initialDestination

    var isReady = false
        private set

    fun doInitialWork() {
        viewModelScope.launch {
            val result = getCurrentUserUseCase(Unit)
            if (result is Result.Success && result.data.isLogged) {
                _initialDestination.emit(Screen.Home.route)
            } else {
                _initialDestination.emit(Screen.Login.route)
            }
            delay(100L)
            isReady = true
        }
    }
}