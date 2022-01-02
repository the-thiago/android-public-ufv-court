package com.ufv.court.ui_profile.changepasword

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ufv.court.R
import com.ufv.court.core.ui.base.rememberFlowWithLifecycle
import com.ufv.court.core.ui.components.*

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
    PasswordChangedDialog(state.showPasswordChangedDialog, action, navigateUp)
    state.error?.let {
        ErrorTreatment(state.error) {
            action(ChangePasswordAction.CleanErrors)
        }
    }
}

@Composable
private fun PasswordChangedDialog(
    show: Boolean,
    action: (ChangePasswordAction) -> Unit,
    navigateUp: () -> Unit
) {
    if (show) {
        OneButtonSuccessDialog(
            title = stringResource(R.string.password_changed),
            message = stringResource(R.string.you_can_use_the_new_password)
        ) {
            action(ChangePasswordAction.ChangeShowPasswordChangedDialog(false))
            navigateUp()
        }
    }
}

@Composable
fun ErrorTreatment(error: Throwable, onDismiss: () -> Unit) {
    when (error) {
        is ChangePasswordError.EmptyField -> {
            OneButtonErrorDialog(
                message = stringResource(id = R.string.empty_field_error),
                onDismiss = onDismiss
            )
        }
        is ChangePasswordError.DifferentPasswords -> {
            OneButtonErrorDialog(
                message = stringResource(id = R.string.different_passwords),
                onDismiss = onDismiss
            )
        }
        is ChangePasswordError.InvalidCredentials -> {
            OneButtonErrorDialog(
                message = stringResource(R.string.invalid_credentials),
                onDismiss = onDismiss
            )
        }
        is ChangePasswordError.NoUserFound -> {
            OneButtonErrorDialog(
                message = stringResource(R.string.invalid_credentials),
                onDismiss = onDismiss
            )
        }
        is ChangePasswordError.AuthWeakPassword -> {
            OneButtonErrorDialog(
                message = stringResource(R.string.invalid_number_of_characters),
                onDismiss = onDismiss
            )
        }
        else -> {
            OneButtonErrorDialog(
                message = stringResource(R.string.unknown_error),
                onDismiss = onDismiss
            )
        }
    }
}

@Composable
private fun TextFieldsAndButton(
    state: ChangePasswordViewState,
    action: (ChangePasswordAction) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ChangePasswordSpacer()
        CustomTextField(
            text = state.currentPassword,
            onTextChange = { action(ChangePasswordAction.ChangeCurrentPassword(it)) },
            labelText = stringResource(R.string.current_password),
            visualTransformation = PasswordVisualTransformation()
        )
        ChangePasswordSpacer()
        CustomTextField(
            text = state.newPassword,
            onTextChange = { action(ChangePasswordAction.ChangeNewPassword(it)) },
            labelText = stringResource(R.string.new_password),
            visualTransformation = PasswordVisualTransformation()
        )
        ChangePasswordSpacer()
        CustomTextField(
            text = state.confirmPassword,
            onTextChange = { action(ChangePasswordAction.ChangeConfirmPassword(it)) },
            labelText = stringResource(R.string.confirm_new_password),
            visualTransformation = PasswordVisualTransformation()
        )
        ChangePasswordSpacer()
        LoadingButton(
            isLoading = state.isLoading,
            buttonText = stringResource(R.string.change),
            onButtonClick = { action(ChangePasswordAction.ChangePasswordClick) }
        )
        ChangePasswordSpacer()
    }
}

@Composable
fun ChangePasswordSpacer() {
    Spacer(modifier = Modifier.height(24.dp))
}
