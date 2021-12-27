package com.ufv.court.app

import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.ufv.court.app.theme.UFVCourtTheme
import com.ufv.court.core.navigation.NavigationCommand
import com.ufv.court.core.navigation.NavigationType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        viewModel.doInitialWork()
        setContent {
            UFVCourtApp()
        }
        val content: View = findViewById(android.R.id.content)
        content.viewTreeObserver.addOnPreDrawListener(
            object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    return if (viewModel.isReady) {
                        content.viewTreeObserver.removeOnPreDrawListener(this)
                        true
                    } else {
                        false
                    }
                }
            }
        )
    }

    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    private fun UFVCourtApp() {
        UFVCourtTheme {
            val navController = rememberAnimatedNavController()
            Surface(color = MaterialTheme.colors.background) {
                AppNavigation(navController = navController)
            }
            ObserveAndNavigate(viewModel = viewModel, navController = navController)
        }
    }
}

@Composable
private fun ObserveAndNavigate(
    viewModel: MainViewModel,
    navController: NavHostController
) {
    LaunchedEffect(true) {
        viewModel.navigationManager.commands.collect { command ->
            when (command) {
                is NavigationCommand.Navigate -> {
                    when (val type = command.type) {
                        NavigationType.NavigateTo -> {
                            navController.navigate(
                                route = command.destination,
                                navOptions = command.navOptions
                            )
                        }
                        is NavigationType.PopUpTo -> {
                            navController.popBackStack(
                                command.destination,
                                type.inclusive
                            )
                        }
                    }
                }
                is NavigationCommand.NavigateUp -> navController.navigateUp()
                is NavigationCommand.PopStackBack -> navController.popBackStack()
            }
        }
    }
}