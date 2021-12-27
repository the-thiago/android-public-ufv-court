package com.ufv.court.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ufv.court.core.navigation.NavigationManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val navigationManager: NavigationManager
) : ViewModel() {

    var isReady = false
        private set

    fun doInitialWork() {
        viewModelScope.launch {
            isReady = true
        }
    }
}