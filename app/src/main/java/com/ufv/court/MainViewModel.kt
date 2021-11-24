package com.ufv.court

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    var isReady = false
        private set

    fun doInitialWork() {
        viewModelScope.launch {
//            delay(600L)
            isReady = true
        }
    }
}