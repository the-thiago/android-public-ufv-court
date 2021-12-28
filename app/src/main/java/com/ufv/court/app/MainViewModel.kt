package com.ufv.court.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavOptions
import com.ufv.court.core.core_common.base.Result
import com.ufv.court.core.navigation.LeafScreen
import com.ufv.court.core.navigation.NavigationManager
import com.ufv.court.core.navigation.Screen
import com.ufv.court.core.user_service.domain.usecase.GetCurrentUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val navigationManager: NavigationManager,
    val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    var isReady = false
        private set

    fun doInitialWork() {
        viewModelScope.launch {
            val result = getCurrentUserUseCase(Unit)
            if (result is Result.Success) {
                navigateToInitialScreen(result.data.isLogged)
            }
            isReady = true
        }
    }

    private fun navigateToInitialScreen(isUserLogged: Boolean) {
        val navOptions = NavOptions.Builder()
            .setPopUpTo(LeafScreen.Login.createRoute(), true)
            .build()
        if (isUserLogged) {
            navigationManager.navigate(Screen.Home.route, navOptions)
        } else {
            navigationManager.navigate(Screen.Login.route, navOptions)
        }
    }
}