package com.ufv.court.core.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

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
    object RegisterCredentials : LeafScreen(Screen.Login, "registercredentials/{name}") {

        override val arguments: List<NamedNavArgument>
            get() = listOf(
                navArgument("name") {
                    type = NavType.StringType
                }
            )

        fun createRoute(name: String): String {
            val mName = if (name.isEmpty()) " " else name
            return "${root.route}/registercredentials/$mName"
        }
    }

    object RegisterUser : LeafScreen(Screen.Login, "registeruser")
    object ForgotPassword : LeafScreen(Screen.Login, "forgotpassword")

    object Home : LeafScreen(Screen.Home, "home")

    object Profile : LeafScreen(Screen.Profile, "profile")
    object ChangePassword : LeafScreen(Screen.Profile, "changepassword")
}