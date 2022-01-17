package com.ufv.court.app

import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.ufv.court.app.theme.UFVCourtTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        viewModel.doInitialWork()
        setContent {
            val startDestination by viewModel.initialDestination.collectAsState()
            UFVCourtApp(startDestination = startDestination)
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
    private fun UFVCourtApp(startDestination: String) {
        UFVCourtTheme {
            val navController = rememberAnimatedNavController()
            Scaffold(bottomBar = { BottomBar(navController) }) {
                AppNavigation(
                    navController = navController,
                    startDestination = startDestination
                )
            }
        }
    }
}