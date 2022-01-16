package com.ufv.court.app

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.ufv.court.core.core_common.util.toEncodedString
import com.ufv.court.core.navigation.LeafScreen
import com.ufv.court.core.navigation.Screen
import com.ufv.court.ui_home.calendar.CalendarScreen
import com.ufv.court.ui_home.home.HomeScreen
import com.ufv.court.ui_home.schedule.ScheduleScreen
import com.ufv.court.ui_login.forgotpassword.ForgotPasswordScreen
import com.ufv.court.ui_login.login.LoginScreen
import com.ufv.court.ui_login.register.RegisterScreen
import com.ufv.court.ui_profile.changepasword.ChangePasswordScreen
import com.ufv.court.ui_profile.editprofile.EditProfileScreen
import com.ufv.court.ui_profile.profile.ProfileScreen

@OptIn(ExperimentalAnimationApi::class)
@Composable
internal fun AppNavigation(navController: NavHostController, startDestination: String) {
    AnimatedNavHost(navController = navController, startDestination = startDestination) {
        addLoginTopLevel(navController)
        addHomeTopLevel(navController)
        addProfileTopLevel(navController)
    }
}

fun NavGraphBuilder.addLoginTopLevel(navController: NavController) {
    navigation(
        route = Screen.Login.route,
        startDestination = LeafScreen.Login.createRoute()
    ) {
        addLogin(navController)
        addRegister(navController)
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
            openRegister = {
                navController.navigate(LeafScreen.Register.createRoute()) {
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
fun NavGraphBuilder.addRegister(navController: NavController) {
    composable(LeafScreen.Register.createRoute()) {
        RegisterScreen(
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
fun NavGraphBuilder.addForgotPassword(navController: NavController) {
    composable(LeafScreen.ForgotPassword.createRoute()) {
        ForgotPasswordScreen(navigateUp = navController::navigateUp)
    }
}

fun NavGraphBuilder.addHomeTopLevel(navController: NavController) {
    navigation(
        route = Screen.Home.route,
        startDestination = LeafScreen.Home.createRoute()
    ) {
        addHome(navController)
        addCalendar(navController)
        addSchedule(navController)
    }
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.addHome(navController: NavController) {
    composable(LeafScreen.Home.createRoute()) {
        HomeScreen(
            openCalendar = {
                navController.navigate(LeafScreen.Calendar.createRoute()) {
                    launchSingleTop = true
                }
            }
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.addCalendar(navController: NavController) {
    composable(LeafScreen.Calendar.createRoute()) {
        CalendarScreen(
            navigateUp = navController::navigateUp,
            openSchedule = {
                navController.navigate(LeafScreen.Schedule.createRoute(date = it.toEncodedString())) {
                    launchSingleTop = true
                }
            }
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.addSchedule(navController: NavController) {
    composable(LeafScreen.Schedule.createRoute()) {
        ScheduleScreen(navigateUp = navController::navigateUp, openHome = {
            navController.popBackStack(route = LeafScreen.Home.createRoute(), inclusive = false)
        })
    }
}

fun NavGraphBuilder.addProfileTopLevel(navController: NavController) {
    navigation(
        route = Screen.Profile.route,
        startDestination = LeafScreen.Profile.createRoute()
    ) {
        addProfile(navController)
        addChangePassword(navController)
        addEditProfile(navController)
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
            },
            openEditProfile = {
                navController.navigate(LeafScreen.EditProfile.createRoute()) {
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

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.addEditProfile(navController: NavController) {
    composable(LeafScreen.EditProfile.createRoute()) {
        EditProfileScreen(navigateUp = navController::navigateUp)
    }
}
