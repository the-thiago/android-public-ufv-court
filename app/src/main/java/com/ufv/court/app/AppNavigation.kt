package com.ufv.court.app

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.ufv.court.core.navigation.LeafScreen
import com.ufv.court.core.navigation.Screen
import com.ufv.court.ui_home.home.HomeScreen
import com.ufv.court.ui_home.home.HomeViewModel
import com.ufv.court.ui_login.forgotpassword.ForgotPasswordScreen
import com.ufv.court.ui_login.login.LoginScreen
import com.ufv.court.ui_login.registercredentials.RegisterCredentialsScreen
import com.ufv.court.ui_login.registeruser.RegisterUserScreen
import com.ufv.court.ui_profile.changepasword.ChangePasswordScreen
import com.ufv.court.ui_profile.profile.ProfileScreen

@OptIn(ExperimentalAnimationApi::class)
@Composable
internal fun AppNavigation(navController: NavHostController, startDestination: String) {
    AnimatedNavHost(navController = navController, startDestination = startDestination) {
        addLoginTopLevel(navController)
        addHomeTopLevel()
        addProfileTopLevel(navController)
    }
}

fun NavGraphBuilder.addLoginTopLevel(navController: NavController) {
    navigation(
        route = Screen.Login.route,
        startDestination = LeafScreen.Login.createRoute()
    ) {
        addLogin(navController)
        addRegisterCredentials(navController)
        addRegisterUser(navController)
        addForgotPassword(navController)
    }
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.addLogin(navController: NavController) {
    composable(LeafScreen.Login.createRoute()) {
        LoginScreen(
            openHome = {
                navController.navigate(LeafScreen.Home.createRoute()) {
                    popUpTo(Screen.Login.route)
                    launchSingleTop = true
                }
            },
            openRegisterUser = {
                navController.navigate(LeafScreen.RegisterUser.createRoute()) {
                    launchSingleTop = true
                    restoreState = true
                }
            },
            openForgotPassword = {
                navController.navigate(LeafScreen.ForgotPassword.createRoute()) {
                    launchSingleTop = true
                }
            }
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.addRegisterCredentials(navController: NavController) {
    composable(LeafScreen.RegisterCredentials.createRoute()) {
        RegisterCredentialsScreen(
            navigateUp = navController::navigateUp,
            openLogin = {
                navController.navigate(LeafScreen.Login.createRoute()) {
                    popUpTo(0)
                    launchSingleTop = true
                }
            }
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.addRegisterUser(navController: NavController) {
    composable(LeafScreen.RegisterUser.createRoute()) {
        RegisterUserScreen(
            navigateUp = navController::navigateUp,
            openRegisterCredentials = { name ->
                navController.navigate(LeafScreen.RegisterCredentials.createRoute(name)) {
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.addForgotPassword(navController: NavController) {
    composable(LeafScreen.ForgotPassword.createRoute()) {
        ForgotPasswordScreen(navigateUp = navController::navigateUp)
    }
}

fun NavGraphBuilder.addHomeTopLevel() {
    navigation(
        route = Screen.Home.route,
        startDestination = LeafScreen.Home.createRoute()
    ) {
        addHome()
    }
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.addHome() {
    composable(LeafScreen.Home.createRoute()) {
        val viewModel = hiltViewModel<HomeViewModel>()
        HomeScreen(viewModel)
    }
}

fun NavGraphBuilder.addProfileTopLevel(navController: NavController) {
    navigation(
        route = Screen.Profile.route,
        startDestination = LeafScreen.Profile.createRoute()
    ) {
        addProfile(navController)
        addChangePassword(navController)
    }
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.addProfile(navController: NavController) {
    composable(LeafScreen.Profile.createRoute()) {
        ProfileScreen(
            openLogin = {
                navController.navigate(LeafScreen.Login.createRoute()) {
                    popUpTo(0)
                    launchSingleTop = true
                }
            },
            openChangePassword = {
                navController.navigate(LeafScreen.ChangePassword.createRoute()) {
                    launchSingleTop = true
                }
            }
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.addChangePassword(navController: NavController) {
    composable(LeafScreen.ChangePassword.createRoute()) {
        ChangePasswordScreen(navigateUp = navController::navigateUp)
    }
}