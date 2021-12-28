package com.ufv.court.core.navigation

import androidx.navigation.NamedNavArgument

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Home : Screen("home")
}

sealed class LeafScreen(
    val root: Screen,
    val route: String
) {

    open val arguments: List<NamedNavArgument> = emptyList()

    fun createRoute() = "${root.route}/$route"

    object Login : LeafScreen(Screen.Login, "login")
    object Register : LeafScreen(Screen.Login, "register")

    object Home : LeafScreen(Screen.Login, "home")
}