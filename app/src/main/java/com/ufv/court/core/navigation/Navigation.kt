package com.ufv.court.core.navigation

import androidx.navigation.NamedNavArgument

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Home : Screen("home")
    object Profile : Screen("profile")
}

sealed class LeafScreen(
    val root: Screen,
    val route: String
) {

    open val arguments: List<NamedNavArgument> = emptyList()

    fun createRoute() = "${root.route}/$route"

    object Login : LeafScreen(Screen.Login, "login")
    object Register : LeafScreen(Screen.Login, "register")
    object ForgotPassword : LeafScreen(Screen.Login, "forgotpassword")

    object Home : LeafScreen(Screen.Home, "home")
    object Calendar : LeafScreen(Screen.Home, "calendar")

    object Profile : LeafScreen(Screen.Profile, "profile")
    object ChangePassword : LeafScreen(Screen.Profile, "changepassword")
    object EditProfile : LeafScreen(Screen.Profile, "editprofile")
}