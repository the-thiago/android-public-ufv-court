package com.ufv.court.ui_login.registeruser

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ufv.court.R
import com.ufv.court.core.ui.base.rememberFlowWithLifecycle
import com.ufv.court.core.ui.components.*

@Composable
fun RegisterUserScreen(
    navigateUp: () -> Unit,
    openRegisterCredentials: (String) -> Unit,
) {
    RegisterUserScreen(
        viewModel = hiltViewModel(),
        navigateUp = navigateUp,
        openRegisterCredentials = openRegisterCredentials
    )
}

@Composable
private fun RegisterUserScreen(
    viewModel: RegisterUserViewModel,
    navigateUp: () -> Unit,
    openRegisterCredentials: (String) -> Unit
) {
    val viewState by rememberFlowWithLifecycle(viewModel.state)
        .collectAsState(initial = RegisterUserViewState.Empty)

    RegisterUserScreen(
        state = viewState,
        navigateUp = navigateUp,
        openRegisterCredentials = openRegisterCredentials
    ) { action ->
        viewModel.submitAction(action)
    }
}

@Composable
private fun RegisterUserScreen(
    state: RegisterUserViewState,
    navigateUp: () -> Unit,
    openRegisterCredentials: (String) -> Unit,
    action: (RegisterUserAction) -> Unit
) {
    Scaffold(topBar = {
        CustomToolbar(
            toolbarText = stringResource(R.string.create_account),
            elevation = 4.dp,
            onLeftButtonClick = navigateUp
        )
    }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            CustomTextField(
                labelText = stringResource(R.string.name),
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
                text = state.name,
            ) {
                action(RegisterUserAction.ChangeName(it))
            }
            Spacer(modifier = Modifier.height(32.dp))
            LoadingButton(isLoading = false, buttonText = stringResource(R.string.next)) {
                action(RegisterUserAction.ContinueClick(onSuccess = openRegisterCredentials))
            }
            Spacer(modifier = Modifier.height(40.dp))
            SimplePageIndicator(selectedPage = 0, amountOfPages = 2)
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
    if (state.error != null && state.error == RegisterUserError.EmptyName) {
        OneButtonErrorDialog(message = stringResource(R.string.type_your_name)) {
            action(RegisterUserAction.CleanErrors)
        }
    }
}
