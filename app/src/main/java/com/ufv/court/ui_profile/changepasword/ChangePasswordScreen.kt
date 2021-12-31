package com.ufv.court.ui_profile.changepasword

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ufv.court.R
import com.ufv.court.core.ui.base.rememberFlowWithLifecycle
import com.ufv.court.core.ui.components.CustomToolbar

@Composable
fun ChangePasswordScreen(navigateUp: () -> Unit) {
    ChangePasswordScreen(viewModel = hiltViewModel(), navigateUp = navigateUp)
}

@Composable
private fun ChangePasswordScreen(
    viewModel: ChangePasswordViewModel,
    navigateUp: () -> Unit
) {
    val viewState by rememberFlowWithLifecycle(viewModel.state)
        .collectAsState(initial = ChangePasswordViewState.Empty)

    ChangePasswordScreen(state = viewState, navigateUp = navigateUp) { action ->
        viewModel.submitAction(action)
    }
}

@Composable
private fun ChangePasswordScreen(
    state: ChangePasswordViewState,
    navigateUp: () -> Unit,
    action: (ChangePasswordAction) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        CustomToolbar(toolbarText = stringResource(id = R.string.change_password)) {
            navigateUp()
        }
        TextFieldsAndButton(state, action)
    }
}

@Composable
private fun TextFieldsAndButton(
    state: ChangePasswordViewState,
    action: (ChangePasswordAction) -> Unit
) {

}

@Composable
fun ChangePasswordSpacer() {
    Spacer(modifier = Modifier.height(24.dp))
}
