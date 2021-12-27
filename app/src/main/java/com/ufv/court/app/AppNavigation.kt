package com.ufv.court.app

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.ufv.court.core.navigation.LeafScreen
import com.ufv.court.core.navigation.Screen
import com.ufv.court.ui_login.login.LoginScreen
import com.ufv.court.ui_login.login.LoginViewModel
import com.ufv.court.ui_login.register.RegisterScreen
import com.ufv.court.ui_login.register.RegisterViewModel

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavigation(navController: NavHostController) {
    AnimatedNavHost(navController = navController, startDestination = Screen.Login.route) {
        addLoginTopLevel()
    }
}

fun NavGraphBuilder.addLoginTopLevel() {
    navigation(
        route = Screen.Login.route,
        startDestination = LeafScreen.Login.createRoute()
    ) {
        addLogin()
        addRegister()
    }
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.addLogin() {
    composable(LeafScreen.Login.createRoute()) {
        val viewModel = hiltViewModel<LoginViewModel>()
        LoginScreen(viewModel)
    }
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.addRegister() {
    composable(LeafScreen.Register.createRoute()) {
        val viewModel = hiltViewModel<RegisterViewModel>()
        RegisterScreen(viewModel)
    }
}