package com.ufv.court

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.ufv.court.ui_login.login.LoginScreen
import com.ufv.court.ui_login.login.LoginViewModel

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavigation(navController: NavHostController) {
    AnimatedNavHost(navController = navController, startDestination = "temporary") {
        composable("temporary") {
            LoginScreen(viewModel = LoginViewModel())
        }
    }
}