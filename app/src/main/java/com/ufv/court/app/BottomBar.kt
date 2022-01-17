package com.ufv.court.app

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.ufv.court.R
import com.ufv.court.core.navigation.LeafScreen
import com.ufv.court.core.navigation.Screen

@Composable
fun BottomBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    AnimatedVisibility(
        visible = currentDestination?.route == LeafScreen.Home.createRoute() ||
                currentDestination?.route == LeafScreen.MySchedule.createRoute() ||
                currentDestination?.route == LeafScreen.Profile.createRoute(),
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it }),
    ) {
        BottomNavigation {
            bottomBarScreens.forEach { screen ->
                BottomBarItem(
                    screen = screen,
                    currentDestination = currentDestination,
                    navController = navController
                )
            }
        }
    }
}

@Composable
private fun RowScope.BottomBarItem(
    screen: BottomBarScreen,
    currentDestination: NavDestination?,
    navController: NavHostController
) {
    BottomNavigationItem(
        label = {
            Text(text = stringResource(id = screen.label))
        },
        icon = {
            Icon(imageVector = screen.icon, contentDescription = null)
        },
        selected = currentDestination?.hierarchy?.any {
            it.route == screen.screen.route
        } == true,
        unselectedContentColor = LocalContentColor.current.copy(alpha = ContentAlpha.disabled),
        onClick = {
            navController.navigate(screen.screen.route) {
                popUpTo(navController.graph.findStartDestination().id)
                launchSingleTop = true
            }
        }
    )
}

data class BottomBarScreen(
    val screen: Screen,
    @StringRes val label: Int,
    val icon: ImageVector
)

val bottomBarScreens = listOf(
    BottomBarScreen(
        screen = Screen.Home,
        label = R.string.home_label,
        icon = Icons.Default.Home
    ),
    BottomBarScreen(
        screen = Screen.MySchedule,
        label = R.string.my_schedule,
        icon = Icons.Default.EventAvailable
    ),
    BottomBarScreen(
        screen = Screen.Profile,
        label = R.string.profile_label,
        icon = Icons.Default.Person
    )
)