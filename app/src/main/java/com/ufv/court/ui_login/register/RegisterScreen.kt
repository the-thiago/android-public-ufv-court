package com.ufv.court.ui_login.register

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.ufv.court.core.ui.base.rememberFlowWithLifecycle

@Composable
fun RegisterScreen(viewModel: RegisterViewModel) {
    val viewState by rememberFlowWithLifecycle(viewModel.state)
        .collectAsState(initial = RegisterViewState.Empty)

    RegisterScreen(viewState) { action ->
        viewModel.submitAction(action)
    }
}

@Composable
fun RegisterScreen(
    state: RegisterViewState,
    action: (RegisterAction) -> Unit
) {
    Text(text = "Register to do")
}