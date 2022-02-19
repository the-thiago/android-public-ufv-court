package com.ufv.court.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.ufv.court.core.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(

) : ViewModel() {

    private val _initialDestination = MutableStateFlow(Screen.Login.route)
    val initialDestination: StateFlow<String> = _initialDestination

    var isReady = false
        private set

    fun doInitialWork() {
        viewModelScope.launch {
            if (FirebaseAuth.getInstance().currentUser != null) {
                _initialDestination.emit(Screen.Home.route)
            } else {
                _initialDestination.emit(Screen.Login.route)
            }
            delay(400L)
            isReady = true
        }
    }

    fun sendFirebaseMessagingToken(token: String) {
        viewModelScope.launch {
            val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return@launch
//            registerTokenUseCase(
//                RegisterTokenUseCase.Params(
//                    id = userId,
//                    deviceId = token,
//                    deviceOs = "android",
//                    deviceType = "android"
//                )
//            )
        }
    }
}
